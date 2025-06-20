package ru.point.expenses.presentation.mvi.expensesHistory

sealed interface ExpensesHistoryIntent {
    data class Load(val accountId: Int) : ExpensesHistoryIntent
    object Retry : ExpensesHistoryIntent
}