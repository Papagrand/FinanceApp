package ru.point.income.presentation.mvi.incomesHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.core.common.AccountPreferences
import ru.point.domain.usecase.GetTransactionHistoryUseCase

class IncomesHistoryViewModelFactory(
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase,
    private val prefs: AccountPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomesHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncomesHistoryViewModel(getTransactionHistoryUseCase, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }
}