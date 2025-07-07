package ru.point.transactions.di

import kotlin.properties.Delegates.notNull

object TransactionDepsStore : TransactionDepsProvider {
    override var transactionDeps: TransactionDeps by notNull()
}
