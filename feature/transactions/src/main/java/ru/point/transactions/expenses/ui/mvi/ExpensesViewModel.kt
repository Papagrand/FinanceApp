package ru.point.transactions.expenses.ui.mvi

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
import ru.point.transactions.expenses.domain.usecase.GetExpensesTodayUseCase
import ru.point.utils.common.AccountPreferences
import ru.point.utils.common.Result
import ru.point.utils.model.AppError
import ru.point.utils.network.NetworkTracker
import javax.inject.Inject

/**
 * ExpensesViewModel
 *
 * Ответственность:
 * - управление потоком MVI-интентов (Load, Retry) через SharedFlow;
 * - загрузка и хранение списка трат за сегодня и их суммы в StateFlow;
 * - эмиссия эффектов (показывать Snackbar) при ошибках;
 * - отслеживание текущего accountId из AccountPreferences.
 *
 */

class ExpensesViewModel @Inject constructor(
    private val getExpensesTodayUseCase: GetExpensesTodayUseCase,
    private val prefs: AccountPreferences,
    internal val tracker: NetworkTracker,
) : ViewModel() {
    private val intents = MutableSharedFlow<ExpensesIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(ExpensesState())
    val state: StateFlow<ExpensesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ExpensesEffect>()
    val effect: SharedFlow<ExpensesEffect> = _effect.asSharedFlow()

    private val _accountId = MutableStateFlow<Int?>(null)

    private val _currency = MutableStateFlow<String?>(null)
    val currency: StateFlow<String?> = _currency.asStateFlow()

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
                    load(id)
                }
        }

        viewModelScope.launch {
            intents.collectLatest { intent ->
                when (intent) {
                    is ExpensesIntent.Load,
                    is ExpensesIntent.Retry,
                    -> {
                        _accountId.value
                            ?.let { load(it) }
                            ?: _effect.emit(
                                ExpensesEffect.ShowSnackbar(
                                    "Account ID ещё не инициализирован",
                                ),
                            )
                    }
                }
            }
        }
    }

    fun dispatch(intent: ExpensesIntent) {
        intents.tryEmit(intent)
    }

    private fun load(accountId: Int) {
        viewModelScope.launch {
            getExpensesTodayUseCase(accountId).collect { result ->
                when (result) {
                    is Result.Loading -> _state.update { it.copy(isLoading = true, error = null) }
                    is Result.Success ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                list = result.data.transactionsList,
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
                        _effect.emit(ExpensesEffect.ShowSnackbar("Ошибка: $msg"))
                    }
                }
            }
        }
    }
}
