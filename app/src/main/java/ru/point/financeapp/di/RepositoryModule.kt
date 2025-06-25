package ru.point.financeapp.di

import dagger.Binds
import dagger.Module
import ru.point.account.data.repositoryImpl.AccountRepositoryImpl
import ru.point.account.domain.repository.AccountRepository
import ru.point.categories.data.repositoryImpl.CategoryRepositoryImpl
import ru.point.categories.domain.repository.CategoryRepository
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    @Singleton
    fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository
}
