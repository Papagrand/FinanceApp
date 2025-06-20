package ru.point.categories.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.categories.domain.usecase.ObserveCategoriesUseCase
import ru.point.core.common.AccountPreferences

class CategoriesViewModelFactory(
    private val useCase: ObserveCategoriesUseCase,
    private val prefs: AccountPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriesViewModel(useCase, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
