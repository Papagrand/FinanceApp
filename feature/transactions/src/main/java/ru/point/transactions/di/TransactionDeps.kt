package ru.point.transactions.di

import ru.point.api.flow.AccountPreferencesRepo
import ru.point.api.repository.CategoryRepository
import ru.point.api.repository.TransactionRepository

interface TransactionDeps {
    val transactionRepository: TransactionRepository
    val categoryRepository: CategoryRepository
    val accountPreferencesRepo: AccountPreferencesRepo
}
