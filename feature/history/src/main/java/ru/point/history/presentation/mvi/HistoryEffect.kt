package ru.point.history.presentation.mvi

sealed interface HistoryEffect {
    data class ShowSnackbar(val message: String) : HistoryEffect
}
