package ru.point.account.ui.mvi.accountEdit

internal sealed interface AccountEditIntent {
    data object Load : AccountEditIntent

    data object Retry : AccountEditIntent

    data object Save : AccountEditIntent

    data object Cancel : AccountEditIntent

    data class ChangeName(val newName: String) : AccountEditIntent

    data class ChangeBalance(val newBalance: String) : AccountEditIntent

    data class ChangeCurrency(val newCurrency: String) : AccountEditIntent
}
