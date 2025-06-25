package ru.point.financeapp.di

import dagger.Module
import dagger.Provides
import ru.point.account.domain.repository.AccountRepository
import ru.point.account.domain.usecase.GetAllAccountsUseCase
import ru.point.categories.domain.repository.CategoryRepository
import ru.point.categories.domain.usecase.ObserveCategoriesUseCase
import javax.inject.Singleton

@Module
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetAllAccountsUseCase(repo: AccountRepository): GetAllAccountsUseCase = GetAllAccountsUseCase(repo)

    @Provides
    @Singleton
    fun provideObserveCategoriesUseCase(repo: CategoryRepository): ObserveCategoriesUseCase = ObserveCategoriesUseCase(repo)
}
