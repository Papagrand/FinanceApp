package ru.point.transactions.addOrEditTransaction.ui.mvi

internal sealed interface AddOrEditTransactionEffect {
    data class ShowSnackbar(val message: String) : AddOrEditTransactionEffect

    data object Finish : AddOrEditTransactionEffect
}
