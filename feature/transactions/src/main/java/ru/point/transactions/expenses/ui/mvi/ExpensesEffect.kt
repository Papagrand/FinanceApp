package ru.point.transactions.expenses.ui.mvi

sealed interface ExpensesEffect {
    data class ShowSnackbar(val message: String) : ExpensesEffect
}
