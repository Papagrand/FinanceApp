package ru.point.transactions.history.di

import dagger.Component
import ru.point.transactions.di.TransactionDeps
import ru.point.transactions.history.ui.mvi.HistoryViewModelFactory

@Component(dependencies = [TransactionDeps::class])
internal interface HistoryComponent {
    @Component.Builder
    interface Builder {
        fun deps(transactionDeps: TransactionDeps): Builder

        fun build(): HistoryComponent
    }

    val historyViewModelFactory: HistoryViewModelFactory
}
