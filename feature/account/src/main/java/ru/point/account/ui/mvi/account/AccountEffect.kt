package ru.point.account.ui.mvi.account

internal sealed interface AccountEffect {
    data class ShowSnackbar(val message: String) : AccountEffect
}
