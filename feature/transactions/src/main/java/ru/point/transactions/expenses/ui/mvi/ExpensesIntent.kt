package ru.point.transactions.expenses.ui.mvi

sealed interface ExpensesIntent {
    data object Load : ExpensesIntent

    data object Retry : ExpensesIntent
}
