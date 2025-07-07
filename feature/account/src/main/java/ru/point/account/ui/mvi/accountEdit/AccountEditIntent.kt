package ru.point.account.ui.mvi.accountEdit

internal sealed interface AccountEditIntent {
    object Load : AccountEditIntent

    object Retry : AccountEditIntent

    object Save : AccountEditIntent

    object Cancel : AccountEditIntent

    data class ChangeName(val newName: String) : AccountEditIntent

    data class ChangeBalance(val newBalance: String) : AccountEditIntent

    data class ChangeCurrency(val newCurrency: String) : AccountEditIntent
}
