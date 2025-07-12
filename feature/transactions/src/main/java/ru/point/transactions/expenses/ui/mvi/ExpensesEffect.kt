package ru.point.transactions.expenses.ui.mvi

internal sealed interface ExpensesEffect {
    data class ShowSnackbar(val message: String) : ExpensesEffect
}
