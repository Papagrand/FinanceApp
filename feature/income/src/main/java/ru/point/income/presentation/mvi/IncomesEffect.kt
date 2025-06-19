package ru.point.income.presentation.mvi

sealed interface IncomesEffect {
    data class ShowSnackbar(val message: String) : IncomesEffect
}