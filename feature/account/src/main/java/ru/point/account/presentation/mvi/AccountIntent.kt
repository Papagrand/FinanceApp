package ru.point.account.presentation.mvi

sealed interface AccountIntent {
    object Load : AccountIntent
    object Retry : AccountIntent
}