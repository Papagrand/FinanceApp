package ru.point.account.di.deps

import kotlin.properties.Delegates.notNull

object AccountDepsStore : AccountDepsProvider {
    override var accountDeps: AccountDeps by notNull()
}
