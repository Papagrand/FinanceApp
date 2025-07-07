package ru.point.account.di.component

import dagger.Component
import ru.point.account.di.deps.AccountDeps
import ru.point.account.ui.mvi.account.AccountViewModelFactory

@Component(dependencies = [AccountDeps::class])
internal interface AccountComponent {
    @Component.Builder
    interface Builder {
        fun deps(accountDeps: AccountDeps): Builder

        fun build(): AccountComponent
    }

    val accountViewModelFactory: AccountViewModelFactory
}
