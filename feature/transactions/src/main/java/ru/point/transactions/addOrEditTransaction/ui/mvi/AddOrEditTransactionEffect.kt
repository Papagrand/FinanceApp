package ru.point.transactions.addOrEditTransaction.ui.mvi

sealed interface AddOrEditTransactionEffect {
    data class ShowSnackbar(val message: String) : AddOrEditTransactionEffect

    data object Finish : AddOrEditTransactionEffect
}
