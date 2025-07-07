package ru.point.transactions.di

import ru.point.api.flow.AccountPreferencesRepo
import ru.point.api.repository.TransactionRepository

interface TransactionDeps {
    val transactionRepository: TransactionRepository
    val accountPreferencesRepo: AccountPreferencesRepo
}
