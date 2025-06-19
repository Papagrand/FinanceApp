package ru.point.income.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.domain.usecase.GetIncomesTodayUseCase

class IncomesViewModelFactory(
    private val getIncomesTodayUseCase: GetIncomesTodayUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncomesViewModel(getIncomesTodayUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }
}