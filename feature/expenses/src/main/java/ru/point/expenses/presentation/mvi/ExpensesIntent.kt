package ru.point.expenses.presentation.mvi

sealed interface ExpensesIntent {
    object Load : ExpensesIntent

    object Retry : ExpensesIntent
}
