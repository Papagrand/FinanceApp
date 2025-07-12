package ru.point.transactions.incomes.ui.mvi

internal sealed interface IncomesEffect {
    data class ShowSnackbar(val message: String) : IncomesEffect
}
