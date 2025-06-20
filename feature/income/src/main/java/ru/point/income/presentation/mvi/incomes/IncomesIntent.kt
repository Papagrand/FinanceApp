package ru.point.income.presentation.mvi.incomes

sealed interface IncomesIntent {
    object Load : IncomesIntent
    object Retry : IncomesIntent
}