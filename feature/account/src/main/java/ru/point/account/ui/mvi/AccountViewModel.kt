package ru.point.account.ui.mvi

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
import ru.point.utils.common.Result
import ru.point.utils.model.AppError
import javax.inject.Inject

/**
 * AccountViewModel
 *
 * Ответственность:
 * - управление потоком MVI-интентов (Load/Retry) через SharedFlow;
 * - загрузка данных аккаунтов через GetAllAccountsUseCase;
 * - обновление StateFlow состояния (isLoading, accountData, error);
 * - эмиссия эффектов (показ Snackbar) через SharedFlow.
 */

class AccountViewModel @Inject constructor(
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
) : ViewModel() {
    private val intents = MutableSharedFlow<AccountIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AccountEffect>()
    val effect: SharedFlow<AccountEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            intents.collectLatest { intent ->
                when (intent) {
                    AccountIntent.Load,
                    AccountIntent.Retry,
                    -> loadAccounts()
                }
            }
        }
        dispatch(AccountIntent.Load)
    }

    fun dispatch(intent: AccountIntent) {
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
                        _state.update {
                            it.copy(
                                isLoading = false,
                                accountData = result.data,
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
                        _effect.emit(AccountEffect.ShowSnackbar("Ошибка: $msg"))
                    }
                }
            }
        }
    }
}
