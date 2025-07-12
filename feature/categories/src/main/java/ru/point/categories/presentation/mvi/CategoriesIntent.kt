package ru.point.categories.presentation.mvi

internal sealed interface CategoriesIntent {
    data object Load : CategoriesIntent

    data object Retry : CategoriesIntent

    data class Search(val query: String) : CategoriesIntent
}
