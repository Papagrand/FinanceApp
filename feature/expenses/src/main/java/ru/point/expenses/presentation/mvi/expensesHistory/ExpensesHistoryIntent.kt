package ru.point.expenses.presentation.mvi.expensesHistory

sealed interface ExpensesHistoryIntent {
    object Load : ExpensesHistoryIntent
    object Retry : ExpensesHistoryIntent
}