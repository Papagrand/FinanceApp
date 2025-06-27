package ru.point.categories.presentation.mvi

sealed interface CategoriesIntent {
    object Load : CategoriesIntent

    object Retry : CategoriesIntent

    data class Search(val query: String) : CategoriesIntent
}
