package ru.point.account.ui.mvi.account

internal sealed interface AccountIntent {
    data object Load : AccountIntent

    data object Retry : AccountIntent
}
