package ru.point.account.ui.mvi.accountEdit

internal sealed interface AccountEditEffect {
    data class ShowSnackbar(val message: String) : AccountEditEffect

    data object Finish : AccountEditEffect
}
