package ru.point.transactions.expenses.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.transactions.expenses.domain.usecase.GetExpensesTodayUseCase
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
internal class ExpensesViewModelFactory @Inject constructor(
    private val getExpensesTodayUseCase: GetExpensesTodayUseCase,
    private val prefs: AccountPreferencesRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ExpensesViewModel(getExpensesTodayUseCase = getExpensesTodayUseCase, prefs) as T
}
