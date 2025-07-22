package ru.point.account.ui.mvi.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.point.account.domain.usecase.GetAllAccountsUseCase
import ru.point.account.domain.usecase.GetChartsUseCase
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.utils.common.Result
import ru.point.utils.model.toUserMessage

/**
 * AccountViewModel
 *
 * Ответственность:
 * - управление потоком MVI-интентов (Load/Retry) через SharedFlow;
 * - загрузка данных аккаунтов через GetAllAccountsUseCase;
 * - обновление StateFlow состояния (isLoading, accountData, error);
 * - эмиссия эффектов (показ Snackbar) через SharedFlow.
 */

internal class AccountViewModel @Inject constructor(
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val getChartsUseCase: GetChartsUseCase,
    private val prefs: AccountPreferencesRepo,
) : ViewModel() {
    private val intents = MutableSharedFlow<AccountIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AccountEffect>()
    val effect: SharedFlow<AccountEffect> = _effect.asSharedFlow()

    private val accountIdFlow = prefs.accountIdFlow.filterNotNull()

    init {
        handleIntents()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleIntents() = viewModelScope.launch {
        val shared = intents.onStart { emit(AccountIntent.Load) }.shareIn(this, SharingStarted.Eagerly, 0)

        merge(
            shared.filter { it is AccountIntent.Load || it is AccountIntent.Retry }
                .flatMapLatest { loadAccounts() },

            shared.filterIsInstance<AccountIntent.RefreshChart>()
                .flatMapLatest { accountIdFlow }
                .flatMapLatest { id -> loadChart(id) }
        ).collect()
    }

    fun dispatch(intent: AccountIntent) {
        viewModelScope.launch {
            intents.emit(intent)
        }
    }

    private fun loadAccounts(): Flow<Unit> = channelFlow {
        getAllAccountsUseCase().collectLatest { result ->
            when (result) {
                is Result.Loading -> _state.update { it.copy(isLoading = true, error = null) }
                is Result.Success -> _state.update { it.copy(isLoading = false, accountData = result.data) }
                is Result.Error -> _effect.emit(AccountEffect.ShowSnackbar(result.cause.toUserMessage()))
            }
        }
        send(Unit)
    }

    private fun loadChart(accId: Int): Flow<Unit> = channelFlow {
        getChartsUseCase(accId).collectLatest { result ->
            when (result) {
                is Result.Loading -> _state.update { it.copy(error = null) }
                is Result.Success -> _state.update { it.copy(chart = result.data) }
                is Result.Error -> _effect.emit(AccountEffect.ShowSnackbar(result.cause.toUserMessage()))
            }
        }
        send(Unit)
    }
}
