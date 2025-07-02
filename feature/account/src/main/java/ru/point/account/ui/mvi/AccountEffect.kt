package ru.point.account.ui.mvi

sealed interface AccountEffect {
    data class ShowSnackbar(val message: String) : AccountEffect
}
