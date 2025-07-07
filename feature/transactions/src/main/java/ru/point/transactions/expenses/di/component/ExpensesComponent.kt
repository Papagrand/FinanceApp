package ru.point.transactions.expenses.di.component

import dagger.Component
import ru.point.transactions.di.TransactionDeps
import ru.point.transactions.expenses.ui.mvi.ExpensesViewModelFactory

@Component(dependencies = [TransactionDeps::class])
internal interface ExpensesComponent {
    @Component.Builder
    interface Builder {
        fun deps(transactionDeps: TransactionDeps): Builder

        fun build(): ExpensesComponent
    }

    val expensesViewModelFactory: ExpensesViewModelFactory
}
