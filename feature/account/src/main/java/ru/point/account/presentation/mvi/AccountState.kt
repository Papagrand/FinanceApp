package ru.point.account.presentation.mvi

import ru.point.account.domain.model.Account

data class AccountState(
    val isLoading: Boolean = false,
    val accountData: Account? = null,
    val error: String? = null,
    val query: String = "",
)
