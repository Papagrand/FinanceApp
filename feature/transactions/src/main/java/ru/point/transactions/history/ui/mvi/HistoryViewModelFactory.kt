package ru.point.transactions.history.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.transactions.history.domain.usecase.GetTransactionHistoryUseCase
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
internal class HistoryViewModelFactory @Inject constructor(
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase,
    private val prefs: AccountPreferencesRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        HistoryViewModel(getTransactionHistoryUseCase = getTransactionHistoryUseCase, prefs) as T
}
