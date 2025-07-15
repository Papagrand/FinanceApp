package ru.point.transactions.analysis.di

import dagger.BindsInstance
import dagger.Component
import ru.point.transactions.analysis.ui.mvi.AnalysisTransactionsViewModelFactory
import ru.point.transactions.di.TransactionDeps

@Component(dependencies = [TransactionDeps::class])
internal interface AnalysisTransactionsComponent {
    @Component.Builder
    interface Builder {
        fun deps(transactionDeps: TransactionDeps): Builder

        @BindsInstance fun isIncome(isIncome: Boolean): Builder

        fun build(): AnalysisTransactionsComponent
    }

    val analysisTransactionsViewModelFactory: AnalysisTransactionsViewModelFactory
}