package ru.point.account.ui.mvi.account

import ru.point.api.model.AccountDto

internal data class AccountState(
    val isLoading: Boolean = false,
    val accountData: AccountDto? = null,
    val error: String? = null,
    val query: String = "",
)
