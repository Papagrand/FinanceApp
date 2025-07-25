package ru.point.account.ui.mvi.account

import ru.point.api.model.AccountDto
import ru.point.api.model.ChartEntry

internal data class AccountState(
    val isLoading: Boolean = false,
    val accountData: AccountDto? = null,
    val chart: List<ChartEntry> = emptyList(),
    val error: String? = null,
    val query: String = "",
)
