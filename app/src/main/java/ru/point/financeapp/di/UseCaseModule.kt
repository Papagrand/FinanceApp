package ru.point.financeapp.di

import dagger.Module
import dagger.Provides
import ru.point.account.domain.usecase.GetAllAccountsUseCase
import ru.point.account.domain.usecase.UpdateAccountUseCase
import ru.point.api.repository.AccountRepository
import ru.point.api.repository.CategoryRepository
import ru.point.api.repository.TransactionRepository
import ru.point.categories.domain.usecase.ObserveCategoriesUseCase
import ru.point.transactions.expenses.domain.usecase.GetExpensesTodayUseCase
import ru.point.transactions.history.domain.usecase.GetTransactionHistoryUseCase
import ru.point.transactions.incomes.domain.usecase.GetIncomesTodayUseCase
import javax.inject.Singleton

/**
 * Модуль бизнес-логики (Use Case).
 *
 * Провайдет единичные экземпляры:
 *  - GetAllAccountsUseCase — получение списка аккаунтов;
 *  - ObserveCategoriesUseCase — подписка на категории;
 *  - GetExpensesTodayUseCase — получение расходов за текущий день;
 *  - GetIncomesTodayUseCase — получение доходов за текущий день;
 *  - GetTransactionHistoryUseCase — получение истории транзакций.
 */

@Module
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetAllAccountsUseCase(repo: AccountRepository): GetAllAccountsUseCase = GetAllAccountsUseCase(repo)

    @Provides
    @Singleton
    fun provideUpdateAccountUseCase(repo: AccountRepository): UpdateAccountUseCase = UpdateAccountUseCase(repo)

    @Provides
    @Singleton
    fun provideObserveCategoriesUseCase(repo: CategoryRepository): ObserveCategoriesUseCase = ObserveCategoriesUseCase(repo)

    @Provides
    @Singleton
    fun provideGetExpensesTodayUseCase(repo: TransactionRepository): GetExpensesTodayUseCase = GetExpensesTodayUseCase(repo)

    @Provides
    @Singleton
    fun provideGetIncomesTodayUseCase(repo: TransactionRepository): GetIncomesTodayUseCase = GetIncomesTodayUseCase(repo)

    @Provides
    @Singleton
    fun provideTransactionUseCase(repo: TransactionRepository): GetTransactionHistoryUseCase = GetTransactionHistoryUseCase(repo)
}
