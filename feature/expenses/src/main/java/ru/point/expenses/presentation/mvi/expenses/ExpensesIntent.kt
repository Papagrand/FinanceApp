package ru.point.expenses.presentation.mvi.expenses

sealed interface ExpensesIntent {
    object Load : ExpensesIntent

    object Retry : ExpensesIntent
}
