package ru.point.expenses.presentation.mvi.expensesHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.SupervisorJob
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
import ru.point.core.common.AccountPreferences
import ru.point.core.common.Result
import ru.point.core.error.AppError
import ru.point.domain.usecase.GetTransactionHistoryUseCase

/**
 * ExpensesHistoryViewModel
 *
 * Ответственность:
 * - управление MVI-потоком интентов (Load, Retry) через SharedFlow;
 * - загрузка списка расходов за выбранный период и вычисление общей суммы;
 * - обновление состояния экрана (isLoading, list, total, error) в StateFlow;
 * - эмиссия побочных эффектов (ShowSnackbar) при ошибках;
 * - отслеживание текущего accountId из AccountPreferences.
 *
 */

class ExpensesHistoryViewModel(
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase,
    private val prefs: AccountPreferences,
) : ViewModel() {
    private val bgJob = SupervisorJob()

    private val intents = MutableSharedFlow<ExpensesHistoryIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(ExpensesHistoryState())
    val state: StateFlow<ExpensesHistoryState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ExpensesHistoryEffect>()
    val effect: SharedFlow<ExpensesHistoryEffect> = _effect.asSharedFlow()

    private val _accountId = MutableStateFlow<Int?>(null)
    val accountId: StateFlow<Int?> = _accountId

    init {
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
                    is ExpensesHistoryIntent.Load,
                    is ExpensesHistoryIntent.Retry,
                    -> {
                        _accountId.value
                            ?.let { load(it) }
                            ?: _effect.emit(
                                ExpensesHistoryEffect.ShowSnackbar(
                                    "Account ID ещё не инициализирован",
                                ),
                            )
                    }
                }
            }
        }
    }

    override fun onCleared() {
        bgJob.cancel()
        super.onCleared()
    }

    fun dispatch(intent: ExpensesHistoryIntent) {
        intents.tryEmit(intent)
    }

    private fun load(accountId: Int) {
        viewModelScope.launch {
            getTransactionHistoryUseCase(accountId).collect { result ->
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
                        _effect.emit(ExpensesHistoryEffect.ShowSnackbar("Ошибка: $msg"))
                    }
                }
            }
        }
    }
}
