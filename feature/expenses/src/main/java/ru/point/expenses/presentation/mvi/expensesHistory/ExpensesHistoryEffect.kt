package ru.point.expenses.presentation.mvi.expensesHistory

sealed interface ExpensesHistoryEffect {
    data class ShowSnackbar(val message: String) : ExpensesHistoryEffect
}