package ru.point.transactions.incomes.ui.mvi

sealed interface IncomesIntent {
    object Load : IncomesIntent

    object Retry : IncomesIntent
}
