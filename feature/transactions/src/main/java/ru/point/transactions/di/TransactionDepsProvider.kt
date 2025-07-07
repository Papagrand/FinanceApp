package ru.point.transactions.di

interface TransactionDepsProvider {
    val transactionDeps: TransactionDeps

    companion object : TransactionDepsProvider by TransactionDepsStore
}
