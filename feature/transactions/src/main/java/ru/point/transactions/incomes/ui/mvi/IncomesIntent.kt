package ru.point.transactions.incomes.ui.mvi

internal sealed interface IncomesIntent {
    data object Load : IncomesIntent

    data object Retry : IncomesIntent
}
