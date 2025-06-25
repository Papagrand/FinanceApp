package ru.point.financeapp.di

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import ru.point.account.domain.usecase.GetAllAccountsUseCase
import ru.point.account.domain.repository.AccountRepository

@Module
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetAllAccountsUseCase(
        repo: AccountRepository
    ): GetAllAccountsUseCase = GetAllAccountsUseCase(repo)
}