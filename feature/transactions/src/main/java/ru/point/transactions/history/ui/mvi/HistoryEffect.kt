package ru.point.transactions.history.ui.mvi

sealed interface HistoryEffect {
    data class ShowSnackbar(val message: String) : HistoryEffect
}
