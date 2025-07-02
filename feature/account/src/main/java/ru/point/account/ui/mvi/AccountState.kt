package ru.point.account.ui.mvi

import ru.point.api.model.AccountDto

data class AccountState(
    val isLoading: Boolean = false,
    val accountData: AccountDto? = null,
    val error: String? = null,
    val query: String = "",
)
