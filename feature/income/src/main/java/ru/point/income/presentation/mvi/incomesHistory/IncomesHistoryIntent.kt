package ru.point.income.presentation.mvi.incomesHistory

sealed interface IncomesHistoryIntent {
    object Load : IncomesHistoryIntent

    object Retry : IncomesHistoryIntent
}
