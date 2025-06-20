package ru.point.expenses.presentation.mvi.expenses

sealed interface ExpensesEffect {
    data class ShowSnackbar(val message: String) : ExpensesEffect
}