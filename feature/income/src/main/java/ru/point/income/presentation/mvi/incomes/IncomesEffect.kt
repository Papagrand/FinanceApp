package ru.point.income.presentation.mvi.incomes

sealed interface IncomesEffect {
    data class ShowSnackbar(val message: String) : IncomesEffect
}
