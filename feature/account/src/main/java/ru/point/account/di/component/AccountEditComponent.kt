package ru.point.account.di.component

import dagger.Component
import ru.point.account.di.deps.AccountDeps
import ru.point.account.ui.mvi.accountEdit.AccountEditViewModelFactory

@Component(dependencies = [AccountDeps::class])
internal interface AccountEditComponent {
    @Component.Builder
    interface Builder {
        fun deps(accountDeps: AccountDeps): Builder

        fun build(): AccountEditComponent
    }

    val accountEditViewModelFactory: AccountEditViewModelFactory
}
