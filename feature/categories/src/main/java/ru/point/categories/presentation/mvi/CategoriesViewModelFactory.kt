package ru.point.categories.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.categories.domain.usecase.ObserveCategoriesUseCase
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
internal class CategoriesViewModelFactory @Inject constructor(
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val prefs: AccountPreferencesRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        CategoriesViewModel(observeCategoriesUseCase = observeCategoriesUseCase, prefs) as T
}
