package ru.point.income.presentation.mvi

sealed interface IncomesIntent {
    data class Load(val accountId: Int) : IncomesIntent
    object Retry : IncomesIntent
}