package ru.point.transactions.history.ui.mvi

sealed interface HistoryIntent {
    object Load : HistoryIntent

    object Retry : HistoryIntent
}
