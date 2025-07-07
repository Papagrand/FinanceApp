package ru.point.account.di.deps

interface AccountDepsProvider {
    val accountDeps: AccountDeps

    companion object : AccountDepsProvider by AccountDepsStore
}
