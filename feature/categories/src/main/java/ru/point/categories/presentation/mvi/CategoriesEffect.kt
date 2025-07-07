package ru.point.categories.presentation.mvi

internal sealed interface CategoriesEffect {
    data class ShowSnackbar(val message: String) : CategoriesEffect
}
