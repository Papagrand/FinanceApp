package ru.point.account.ui.mvi.accountEdit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.point.account.domain.usecase.GetAllAccountsUseCase
import ru.point.account.domain.usecase.UpdateAccountUseCase
import ru.point.utils.common.AccountPreferences
import ru.point.utils.common.Result
import ru.point.utils.extensionsAndParsers.validateBalance
import ru.point.utils.model.AppError
import ru.point.utils.network.NetworkTracker
import javax.inject.Inject

class AccountEditViewModel @Inject constructor(
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val prefs: AccountPreferences,
    internal val tracker: NetworkTracker,
) : ViewModel() {
    private val intents = MutableSharedFlow<AccountEditIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(AccountEditState())
    val state: StateFlow<AccountEditState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AccountEditEffect>()
    val effect: SharedFlow<AccountEditEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            intents.collectLatest { intent ->
                when (intent) {
                    is AccountEditIntent.Load -> loadAccounts()
                    is AccountEditIntent.Retry -> loadAccounts()
                    is AccountEditIntent.ChangeName -> changeName(intent.newName)
                    is AccountEditIntent.ChangeBalance -> changeBalance(intent.newBalance)
                    is AccountEditIntent.ChangeCurrency -> changeCurrency(intent.newCurrency)
                    AccountEditIntent.Save -> saveChanges()
                    AccountEditIntent.Cancel -> finishEditing()
                }
            }
        }
        dispatch(AccountEditIntent.Load)
    }

    fun dispatch(intent: AccountEditIntent) {
        viewModelScope.launch {
            intents.emit(intent)
        }
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            getAllAccountsUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }

                    is Result.Success -> {
                        val dto = result.data
                        _state.update {
                            it.copy(
                                isLoading = false,
                                accountData = dto,
                                name = dto.name,
                                balance = dto.balance,
                                currency = dto.currency,
                                isDirty = false,
                                error = null,
                            )
                        }
                    }

                    is Result.Error -> {
                        Log.e("WhyERROR", result.cause.toString())
                        val msg =
                            when (val cause = result.cause) {
                                AppError.BadRequest -> "Неверный формат данных"
                                AppError.Unauthorized -> "Неавторизованный доступ"
                                AppError.NoInternet -> "Нет подключения к интернету"
                                is AppError.ServerError -> "Сервер временно недоступен"
                                is AppError.Http -> "HTTP ${cause.code}: ${cause.body ?: "Ошибка"}"
                                else -> "Неизвестная ошибка"
                            }
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = msg,
                            )
                        }
                        _effect.emit(AccountEditEffect.ShowSnackbar("Ошибка: $msg"))
                    }
                }
            }
        }
    }

    private fun changeName(newName: String) {
        _state.update { st ->
            st.copy(
                name = newName,
                isDirty = newName != st.accountData?.name,
            )
        }
    }

    private fun changeBalance(input: String) {
        val tooLong = input.length > 15

        val normalized = if (tooLong) null else input.validateBalance()
        val isValid = normalized != null

        val errText =
            when {
                input.isBlank() -> "Введите сумму"
                tooLong -> "Не более 13 символов до десятых"
                !isValid -> "Формат: 12345.67"
                else -> null
            }

        _state.update { st ->
            st.copy(
                balance = input,
                balanceValid = isValid,
                balanceError = errText,
                isDirty = isValid && normalized != st.accountData?.balance,
            )
        }
    }

    private fun changeCurrency(newCurrency: String) {
        _state.update { st ->
            st.copy(
                currency = newCurrency,
                isDirty = newCurrency != st.accountData?.currency,
            )
        }
    }

    private fun saveChanges() {
        viewModelScope.launch {
            if (!_state.value.balanceValid) {
                _effect.emit(AccountEditEffect.ShowSnackbar("Неверный формат суммы"))
                return@launch
            }
            updateAccountUseCase(
                id = _state.value.accountData!!.id,
                name = _state.value.name,
                balance = _state.value.balance,
                currency = _state.value.currency,
            ).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }

                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isDirty = false,
                                accountData = result.data,
                            )
                        }
                        prefs.saveCurrency(result.data.currency)
                        _effect.emit(AccountEditEffect.Finish)
                    }

                    is Result.Error -> {
                        val msg =
                            when (val cause = result.cause) {
                                AppError.BadRequest -> "Неверный формат данных"
                                AppError.Unauthorized -> "Не авторизованный доступ"
                                AppError.NoInternet -> "Нет подключения к интернету"
                                is AppError.ServerError -> "Сервер временно недоступен"
                                is AppError.Http -> "HTTP ${cause.code}: ${cause.body ?: "Ошибка"}"
                                else -> "Неизвестная ошибка"
                            }
                        _state.update { it.copy(isLoading = false, error = msg) }
                        _effect.emit(AccountEditEffect.ShowSnackbar("Ошибка: $msg"))
                    }
                }
            }
        }
    }

    private fun finishEditing() {
        viewModelScope.launch {
            _effect.emit(AccountEditEffect.Finish)
        }
    }
}
