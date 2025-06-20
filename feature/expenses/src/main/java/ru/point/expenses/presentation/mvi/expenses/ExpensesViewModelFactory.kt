package ru.point.expenses.presentation.mvi.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.core.common.AccountPreferences
import ru.point.domain.usecase.GetExpensesTodayUseCase

class ExpensesViewModelFactory(
    private val getExpensesTodayUseCase: GetExpensesTodayUseCase,
    private val prefs: AccountPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpensesViewModel(
                getExpensesTodayUseCase,
                prefs
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }
}