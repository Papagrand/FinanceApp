package ru.point.account.ui.mvi.account

sealed interface AccountIntent {
    data object Load : AccountIntent

    data object Retry : AccountIntent
}
