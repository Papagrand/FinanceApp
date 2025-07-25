package ru.point.account.di.deps

import ru.point.api.flow.AccountPreferencesRepo
import ru.point.api.repository.AccountRepository
import ru.point.api.repository.TransactionRepository

interface AccountDeps {
    val accountRepository: AccountRepository
    val transactionRepository: TransactionRepository
    val accountPreferences: AccountPreferencesRepo
}
