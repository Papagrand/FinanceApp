package ru.point.transactions.expenses.ui.mvi

sealed interface ExpensesIntent {
    object Load : ExpensesIntent

    object Retry : ExpensesIntent
}
