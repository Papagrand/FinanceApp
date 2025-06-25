package ru.point.financeapp.di

import dagger.Binds
import dagger.Module
import ru.point.account.data.repositoryImpl.AccountRepositoryImpl
import ru.point.account.domain.repository.AccountRepository
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository
}
