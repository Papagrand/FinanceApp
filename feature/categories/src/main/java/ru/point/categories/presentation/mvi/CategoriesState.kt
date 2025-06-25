package ru.point.categories.presentation.mvi

import ru.point.categories.domain.model.Category

data class CategoriesState(
    val isLoading: Boolean = false,
    val list: List<Category> = emptyList(),
    val error: String? = null,
    val query: String = "",
)
