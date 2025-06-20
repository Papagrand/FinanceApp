package ru.point.expenses.presentation.mvi.expensesHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.domain.usecase.GetTransactionHistoryUseCase

class ExpensesHistoryViewModelFactory(
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpensesHistoryViewModel(getTransactionHistoryUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }
}