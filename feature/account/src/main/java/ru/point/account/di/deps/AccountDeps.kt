package ru.point.account.di.deps

import ru.point.api.flow.AccountPreferencesRepo
import ru.point.api.repository.AccountRepository

interface AccountDeps {
    val accountRepository: AccountRepository
    val accountPreferences: AccountPreferencesRepo
}
