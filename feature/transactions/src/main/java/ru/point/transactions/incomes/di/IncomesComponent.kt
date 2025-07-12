package ru.point.transactions.incomes.di

import dagger.Component
import ru.point.transactions.di.TransactionDeps
import ru.point.transactions.incomes.ui.mvi.IncomesViewModelFactory

@Component(dependencies = [TransactionDeps::class])
internal interface IncomesComponent {
    @Component.Builder
    interface Builder {
        fun deps(transactionDeps: TransactionDeps): Builder

        fun build(): IncomesComponent
    }

    val incomesViewModelFactory: IncomesViewModelFactory
}
