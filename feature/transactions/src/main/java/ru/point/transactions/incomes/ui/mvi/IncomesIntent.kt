package ru.point.transactions.incomes.ui.mvi

sealed interface IncomesIntent {
    data object Load : IncomesIntent

    data object Retry : IncomesIntent
}
