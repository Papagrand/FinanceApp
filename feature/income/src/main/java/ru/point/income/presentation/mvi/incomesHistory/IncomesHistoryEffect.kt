package ru.point.income.presentation.mvi.incomesHistory

sealed interface IncomesHistoryEffect {
    data class ShowSnackbar(val message: String) : IncomesHistoryEffect
}
