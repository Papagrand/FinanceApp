package ru.point.transactions.addOrEditTransaction.di

import dagger.BindsInstance
import dagger.Component
import ru.point.transactions.addOrEditTransaction.ui.mvi.AddOrEditTransactionViewModelFactory
import ru.point.transactions.di.TransactionDeps

@Component(dependencies = [TransactionDeps::class])
internal interface AddOrEditTransactionComponent {
    @Component.Builder
    interface Builder {
        fun deps(transactionDeps: TransactionDeps): Builder

        @BindsInstance fun transactionId(transactionId: Int?): Builder

        @BindsInstance fun isIncome(isIncome: Boolean): Builder

        fun build(): AddOrEditTransactionComponent
    }

    val addOrEditTransactionViewModelFactory: AddOrEditTransactionViewModelFactory
}
