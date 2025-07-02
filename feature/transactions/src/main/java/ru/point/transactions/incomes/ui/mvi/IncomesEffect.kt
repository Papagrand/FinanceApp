package ru.point.transactions.incomes.ui.mvi

sealed interface IncomesEffect {
    data class ShowSnackbar(val message: String) : IncomesEffect
}
