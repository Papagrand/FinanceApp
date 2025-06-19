package ru.point.account.presentation.mvi

import ru.point.account.domain.model.Account

data class AccountState(
    val isLoading: Boolean = false,
    val list: List<Account> = emptyList(),
    val error: String? = null,
    val query: String = ""
)