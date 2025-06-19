package ru.point.account.presentation.mvi

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
import ru.point.account.domain.usecase.GetAllAccountsUseCase
import ru.point.core.common.Result
import ru.point.core.error.AppError

class AccountViewModel(
    private val getAllAccountsUseCase: GetAllAccountsUseCase
) : ViewModel() {

    private val bgJob = SupervisorJob()
    private val ioScope = CoroutineScope(Dispatchers.IO + bgJob)

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
                    AccountIntent.Retry -> loadAccounts()

                }
            }
        }

        dispatch(AccountIntent.Load)
    }

    override fun onCleared() {
        bgJob.cancel()
        super.onCleared()
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
                                list = result.data,
                                error = null
                            )
                        }
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
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = msg
                            )
                        }
                        _effect.emit(AccountEffect.ShowSnackbar("Ошибка: $msg"))
                    }
                }
            }
        }
    }

}