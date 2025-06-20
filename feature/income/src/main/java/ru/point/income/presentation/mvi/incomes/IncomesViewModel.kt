package ru.point.income.presentation.mvi.incomes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.point.core.common.Result
import ru.point.core.error.AppError
import ru.point.domain.usecase.GetIncomesTodayUseCase

class IncomesViewModel(
    private val getIncomesTodayUseCase: GetIncomesTodayUseCase
) : ViewModel() {

    private val bgJob = SupervisorJob()
    private val ioScope = CoroutineScope(Dispatchers.IO + bgJob)

    private val intents = MutableSharedFlow<IncomesIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(IncomesState())
    val state: StateFlow<IncomesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<IncomesEffect>()
    val effect: SharedFlow<IncomesEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            intents.collectLatest { intent ->
                when (intent) {
                    is IncomesIntent.Load,
                    is IncomesIntent.Retry -> load(65) //TODO переделать хардкод
                }
            }
        }
    }

    override fun onCleared() {
        bgJob.cancel()
        super.onCleared()
    }

    fun dispatch(intent: IncomesIntent) {
        intents.tryEmit(intent)
    }

    private fun load(accountId: Int) {
        viewModelScope.launch {
            getIncomesTodayUseCase(accountId).collect { result ->
                when (result) {
                    is Result.Loading -> _state.update { it.copy(isLoading = true, error = null) }
                    is Result.Success -> _state.update {
                        it.copy(
                            isLoading = false,
                            list = result.data.list,
                            total = result.data.total,
                            error = null
                        )
                    }

                    is Result.Error -> {
                        val msg = when (val cause = result.cause) {
                            AppError.BadRequest -> "Неверный формат данных"
                            AppError.Unauthorized -> "Неавторизованный доступ"
                            AppError.NoInternet -> "Нет подключения к интернету"
                            is AppError.ServerError -> "Сервер временно недоступен"
                            is AppError.Http -> "HTTP ${cause.code}: ${cause.body ?: "Ошибка"}"
                            else -> result.cause.toString()
                        }
                        _state.update { it.copy(isLoading = false, error = msg) }
                        _effect.emit(IncomesEffect.ShowSnackbar("Ошибка: $msg"))
                    }
                }
            }
        }
    }


}