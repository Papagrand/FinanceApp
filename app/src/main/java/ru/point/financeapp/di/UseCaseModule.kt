package ru.point.financeapp.di

import dagger.Module
import dagger.Provides
import ru.point.account.domain.repository.AccountRepository
import ru.point.account.domain.usecase.GetAllAccountsUseCase
import ru.point.categories.domain.repository.CategoryRepository
import ru.point.categories.domain.usecase.ObserveCategoriesUseCase
import ru.point.core.di.ExpenseHistory
import ru.point.core.di.IncomeHistory
import ru.point.domain.repository.TransactionRepository
import ru.point.domain.usecase.GetExpensesTodayUseCase
import ru.point.domain.usecase.GetIncomesTodayUseCase
import ru.point.domain.usecase.GetTransactionHistoryUseCase
import javax.inject.Singleton

@Module
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetAllAccountsUseCase(repo: AccountRepository): GetAllAccountsUseCase = GetAllAccountsUseCase(repo)

    @Provides
    @Singleton
    fun provideObserveCategoriesUseCase(repo: CategoryRepository): ObserveCategoriesUseCase = ObserveCategoriesUseCase(repo)

    @Provides
    @Singleton
    fun provideGetExpensesTodayUseCase(repo: TransactionRepository): GetExpensesTodayUseCase = GetExpensesTodayUseCase(repo)

    @Provides
    @Singleton
    fun provideGetIncomesTodayUseCase(repo: TransactionRepository): GetIncomesTodayUseCase = GetIncomesTodayUseCase(repo)

    @Provides @Singleton
    @IncomeHistory
    fun provideIncomesUseCase(repo: TransactionRepository): GetTransactionHistoryUseCase = GetTransactionHistoryUseCase(repo, true)

    @Provides @Singleton
    @ExpenseHistory
    fun provideExpensesUseCase(repo: TransactionRepository): GetTransactionHistoryUseCase = GetTransactionHistoryUseCase(repo, false)
}
