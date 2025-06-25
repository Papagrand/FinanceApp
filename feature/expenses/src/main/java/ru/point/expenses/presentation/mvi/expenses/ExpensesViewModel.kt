package ru.point.expenses.presentation.mvi.expenses

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
import ru.point.domain.usecase.GetExpensesTodayUseCase

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

class ExpensesViewModel(
    private val getExpensesTodayUseCase: GetExpensesTodayUseCase,
    private val prefs: AccountPreferences,
) : ViewModel() {
    private val bgJob = SupervisorJob()

    private val intents = MutableSharedFlow<ExpensesIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(ExpensesState())
    val state: StateFlow<ExpensesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ExpensesEffect>()
    val effect: SharedFlow<ExpensesEffect> = _effect.asSharedFlow()

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

    override fun onCleared() {
        bgJob.cancel()
        super.onCleared()
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
                        _effect.emit(ExpensesEffect.ShowSnackbar("Ошибка: $msg"))
                    }
                }
            }
        }
    }
}
