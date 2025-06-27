package ru.point.account.presentation.mvi

sealed interface AccountEffect {
    data class ShowSnackbar(val message: String) : AccountEffect
}
