package ru.point.transactions.history.ui.mvi

internal sealed interface HistoryEffect {
    data class ShowSnackbar(val message: String) : HistoryEffect
}
