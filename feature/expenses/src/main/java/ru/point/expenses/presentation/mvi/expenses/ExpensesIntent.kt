package ru.point.expenses.presentation.mvi.expenses

sealed interface ExpensesIntent {
    data class Load(val accountId: Int) : ExpensesIntent
    object Retry : ExpensesIntent
}