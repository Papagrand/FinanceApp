package ru.point.income.presentation.mvi.incomes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.core.common.AccountPreferences
import ru.point.domain.usecase.GetIncomesTodayUseCase

class IncomesViewModelFactory(
    private val getIncomesTodayUseCase: GetIncomesTodayUseCase,
    private val prefs: AccountPreferences

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncomesViewModel(getIncomesTodayUseCase, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }
}