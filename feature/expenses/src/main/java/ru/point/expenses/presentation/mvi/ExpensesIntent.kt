package ru.point.expenses.presentation.mvi

sealed interface ExpensesIntent {
    data class Load(val accountId: Int) : ExpensesIntent
    object Retry : ExpensesIntent
}