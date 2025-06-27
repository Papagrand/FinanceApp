package ru.point.categories.presentation.mvi

sealed interface CategoriesEffect {
    data class ShowSnackbar(val message: String) : CategoriesEffect
}
