package ru.point.account.di.deps

import ru.point.api.flow.AccountPreferencesRepo
import ru.point.api.repository.AccountRepository
import ru.point.api.repository.ChartRepository

interface AccountDeps {
    val accountRepository: AccountRepository
    val chartRepository: ChartRepository
    val accountPreferences: AccountPreferencesRepo
}
