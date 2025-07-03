package ru.point.account.ui.mvi.account

sealed interface AccountEffect {
    data class ShowSnackbar(val message: String) : AccountEffect
}
