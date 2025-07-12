package ru.point.transactions.history.ui.mvi

internal sealed interface HistoryIntent {
    data object Load : HistoryIntent

    data object Retry : HistoryIntent
}
