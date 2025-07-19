package ru.point.transactions.analysis.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.transactions.analysis.domain.GetAnalysisTransactionsUseCase

@Suppress("UNCHECKED_CAST")
internal class AnalysisTransactionsViewModelFactory @Inject constructor(
    private val isIncome: Boolean,
    private val getAnalysisTransactionsUseCase: GetAnalysisTransactionsUseCase,
    private val prefs: AccountPreferencesRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        AnalysisTransactionsViewModel(
            isIncome = isIncome,
            getAnalysisTransactionsUseCase = getAnalysisTransactionsUseCase,
            prefs,
        ) as T
}
