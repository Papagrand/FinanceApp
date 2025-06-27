package ru.point.history.presentation.mvi

sealed interface HistoryIntent {
    object Load : HistoryIntent

    object Retry : HistoryIntent
}
