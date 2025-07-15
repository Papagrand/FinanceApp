package ru.point.transactions.analysis.ui.mvi

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
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.utils.common.Result
import ru.point.utils.model.toUserMessage
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import ru.point.transactions.analysis.domain.GetAnalysisTransactionsUseCase

internal class AnalysisTransactionsViewModel @Inject constructor(
    private val isIncome: Boolean,
    private val getAnalysisTransactionsUseCase: GetAnalysisTransactionsUseCase,
    private val prefs: AccountPreferencesRepo,
) : ViewModel() {
    private val intents = MutableSharedFlow<AnalysisTransactionsIntent>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow(AnalysisTransactionsState())
    val state: StateFlow<AnalysisTransactionsState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AnalysisTransactionsEffect>(
        replay = 1,
        extraBufferCapacity = 0
    )
    val effect: SharedFlow<AnalysisTransactionsEffect> = _effect.asSharedFlow()

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
                }
        }

        viewModelScope.launch {
            intents.collectLatest { intent ->
                when (intent) {
                    AnalysisTransactionsIntent.Load -> {
                        openLoadScreen()

                        val accountId = _accountId
                            .filterNotNull()
                            .first()

                        getSummaryCategories(accountId)
                    }

                    AnalysisTransactionsIntent.Retry -> {
                        val id = _accountId.value
                        if (id != null) {
                            getSummaryCategories(id)
                        } else {
                            _effect.emit(
                                AnalysisTransactionsEffect.ShowSnackbar(
                                    "Account ID ещё не инициализирован"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun dispatch(intent: AnalysisTransactionsIntent) {
        intents.tryEmit(intent)
    }

    fun openLoadScreen() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
        }
    }

    private fun getSummaryCategories(accountId: Int) {
        viewModelScope.launch {
            getAnalysisTransactionsUseCase(
                accountId = accountId,
                isIncome = isIncome,
                startDate = _state.value.startDate,
                endDate = _state.value.endDate,
            ).collect { result ->
                when (result) {
                    is Result.Loading -> _state.update { it.copy(isLoading = true, error = null) }
                    is Result.Error -> {
                        val msg = result.cause.toUserMessage()
                        _state.update { it.copy(isLoading = false, error = msg) }
                    }

                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                grandAmountSummary = result.data.grandTotalAmount,
                                categorySummaries = result.data.categorySummaries,
                                error = null,
                            )
                        }
                    }
                }
            }
        }
    }

}
