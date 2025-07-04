package ru.point.transactions.history.ui.mvi

sealed interface HistoryIntent {
    data object Load : HistoryIntent

    data object Retry : HistoryIntent
}
