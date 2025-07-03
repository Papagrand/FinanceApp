package ru.point.account.ui.mvi.account

sealed interface AccountIntent {
    object Load : AccountIntent

    object Retry : AccountIntent
}
