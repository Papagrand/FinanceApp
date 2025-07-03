package ru.point.transactions.history.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.point.transactions.history.domain.usecase.GetTransactionHistoryUseCase
import ru.point.utils.common.AccountPreferences
import ru.point.utils.common.Result
import ru.point.utils.model.AppError
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase,
    private val prefs: AccountPreferences,
) : ViewModel() {
    private var isIncomeFlag: Boolean = false

    private val intents = MutableSharedFlow<HistoryIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HistoryEffect>()
    val effect: SharedFlow<HistoryEffect> = _effect.asSharedFlow()

    private val _accountId = MutableStateFlow<Int?>(null)

    private val _currency = MutableStateFlow("RUB")
    val currency: StateFlow<String> = _currency.asStateFlow()

    init {
        viewModelScope.launch {
            prefs.currencyFlow
                .filterNotNull()
                .collectLatest { _currency.value = it }
        }

        viewModelScope.launch {
            prefs.accountIdFlow
                .filterNotNull()
                .collectLatest { id ->
                    _accountId.value = id
                    load(id, isIncomeFlag)
                }
        }

        viewModelScope.launch {
            intents.collectLatest { intent ->
                when (intent) {
                    is HistoryIntent.Load,
                    is HistoryIntent.Retry,
                    -> {
                        _accountId.value
                            ?.let { load(it, isIncomeFlag) }
                            ?: _effect.emit(
                                HistoryEffect.ShowSnackbar(
                                    "Account ID ещё не инициализирован",
                                ),
                            )
                    }
                }
            }
        }
    }

    fun dispatch(
        intent: HistoryIntent,
        isIncome: Boolean,
    ) {
        isIncomeFlag = isIncome
        intents.tryEmit(intent)
    }

    private fun load(
        accountId: Int,
        isIncome: Boolean,
    ) {
        viewModelScope.launch {
            getTransactionHistoryUseCase(accountId, isIncome).collect { result ->
                when (result) {
                    is Result.Loading -> _state.update { it.copy(isLoading = true, error = null) }
                    is Result.Success ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                list = result.data.list,
                                total = result.data.total,
                                error = null,
                            )
                        }

                    is Result.Error -> {
                        val msg =
                            when (val cause = result.cause) {
                                AppError.BadRequest -> "Неверный формат данных"
                                AppError.Unauthorized -> "Неавторизованный доступ"
                                AppError.NoInternet -> "Нет подключения к интернету"
                                is AppError.ServerError -> "Сервер временно недоступен"
                                is AppError.Http -> "HTTP ${cause.code}: ${cause.body ?: "Ошибка"}"
                                else -> "Неизвестная ошибка"
                            }
                        _state.update { it.copy(isLoading = false, error = msg) }
                        _effect.emit(HistoryEffect.ShowSnackbar("Ошибка: $msg"))
                    }
                }
            }
        }
    }
}
