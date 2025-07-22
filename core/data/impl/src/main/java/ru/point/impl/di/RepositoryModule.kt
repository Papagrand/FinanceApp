package ru.point.impl.di

import dagger.Binds
import dagger.Module
import ru.point.api.repository.AccountRepository
import ru.point.api.repository.CategoryRepository
import ru.point.api.repository.TransactionRepository
import ru.point.impl.repository.AccountRepositoryImpl
import ru.point.impl.repository.CategoryRepositoryImpl
import ru.point.impl.repository.TransactionRepositoryImpl
import javax.inject.Singleton
import ru.point.api.repository.ChartRepository
import ru.point.impl.repository.ChartRepositoryImpl

/**
 * RepositoryModule
 *
 * Соединяет интерфейсы:
 *  - AccountRepository <-> AccountRepositoryImpl
 *  - CategoryRepository <-> CategoryRepositoryImpl
 *  - TransactionRepository <-> TransactionRepositoryImpl
 *
 * Все репозитории зарегистрированы как @Singleton.
 */

@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    @Singleton
    fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    @Singleton
    fun bindChartRepository(impl: ChartRepositoryImpl): ChartRepository
}
