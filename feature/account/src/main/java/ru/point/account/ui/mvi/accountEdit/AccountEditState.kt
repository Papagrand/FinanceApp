package ru.point.account.ui.mvi.accountEdit

import ru.point.api.model.AccountDto

internal data class AccountEditState(
    val isLoading: Boolean = false,
    val accountData: AccountDto? = null,
    val name: String = "",
    val balance: String = "",
    val balanceValid: Boolean = true,
    val balanceError: String? = null,
    val currency: String = "",
    val isDirty: Boolean = false,
    val error: String? = null,
)
