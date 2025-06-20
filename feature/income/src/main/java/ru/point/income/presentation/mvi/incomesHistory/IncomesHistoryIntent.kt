package ru.point.income.presentation.mvi.incomesHistory

sealed interface IncomesHistoryIntent {
    data class Load(val accountId: Int) : IncomesHistoryIntent
    object Retry : IncomesHistoryIntent
}