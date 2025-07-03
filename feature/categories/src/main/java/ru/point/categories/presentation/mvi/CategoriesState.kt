package ru.point.categories.presentation.mvi

import ru.point.api.model.CategoryDto

data class CategoriesState(
    val isLoading: Boolean = false,
    val list: List<CategoryDto> = emptyList(),
    val rawList: List<CategoryDto> = emptyList(),
    val error: String? = null,
    val query: String = "",
)
