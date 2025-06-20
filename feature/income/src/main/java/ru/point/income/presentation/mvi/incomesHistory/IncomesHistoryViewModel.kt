package ru.point.income.presentation.mvi.incomesHistory

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
import ru.point.domain.usecase.GetTransactionHistoryUseCase
import ru.point.network.BuildConfig

class IncomesHistoryViewModel(
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase
) : ViewModel() {

    private val bgJob = SupervisorJob()
    private val ioScope = CoroutineScope(Dispatchers.IO + bgJob)

    private val intents = MutableSharedFlow<IncomesHistoryIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(IncomesHistoryState())
    val state: StateFlow<IncomesHistoryState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<IncomesHistoryEffect>()
    val effect: SharedFlow<IncomesHistoryEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            intents.collectLatest { intent ->
                when (intent) {
                    is IncomesHistoryIntent.Load,
                    is IncomesHistoryIntent.Retry -> load(BuildConfig.ACCOUNT_ID.toInt()) //Todo пока без кеша, определяю в local.properties
                }
            }
        }
    }

    override fun onCleared() {
        bgJob.cancel()
        super.onCleared()
    }

    fun dispatch(intent: IncomesHistoryIntent) {
        intents.tryEmit(intent)
    }

    private fun load(accountId: Int) {
        viewModelScope.launch {
            getTransactionHistoryUseCase(accountId).collect { result ->
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
                            else -> "Неизвестная ошибка"
                        }
                        _state.update { it.copy(isLoading = false, error = msg) }
                        _effect.emit(IncomesHistoryEffect.ShowSnackbar("Ошибка: $msg"))
                    }
                }
            }
        }
    }


}