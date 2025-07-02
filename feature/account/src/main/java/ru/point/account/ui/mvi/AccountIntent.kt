package ru.point.account.ui.mvi

sealed interface AccountIntent {
    object Load : AccountIntent

    object Retry : AccountIntent
}
