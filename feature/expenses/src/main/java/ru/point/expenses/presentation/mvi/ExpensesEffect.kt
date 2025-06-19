package ru.point.expenses.presentation.mvi

sealed interface ExpensesEffect {
    data class ShowSnackbar(val message: String) : ExpensesEffect
}