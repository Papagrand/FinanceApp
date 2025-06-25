package ru.point.financeapp.di

import dagger.Binds
import dagger.Module
import javax.inject.Singleton
import ru.point.account.data.repositoryImpl.AccountRepositoryImpl
import ru.point.account.domain.repository.AccountRepository

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAccountRepository(
        impl: AccountRepositoryImpl
    ): AccountRepository
}