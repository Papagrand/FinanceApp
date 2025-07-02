package ru.point.impl.di

import android.content.Context
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.point.impl.service.AccountService
import ru.point.impl.service.CategoryService
import ru.point.impl.service.TransactionService
import ru.point.utils.network.NetworkTracker
import ru.point.utils.network.RetrofitProvider
import javax.inject.Singleton

/**
 * NetworkModule
 *
 * Предоставляет:
 *  - NetworkTracker — подписка на изменения сети;
 *  - Retrofit — настроенный клиент для REST API (инициализируется через RetrofitProvider);
 *  - AccountService — API для работы с аккаунтами;
 *  - CategoryService — API для работы с категориями;
 *  - TransactionService — API для работы с транзакциями.
 */

@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkTracker(context: Context): NetworkTracker {
        return NetworkTracker(context)
    }

    @Provides
    @Singleton
    fun provideRetrofit(tracker: NetworkTracker): Retrofit {
        RetrofitProvider.init(tracker)
        return RetrofitProvider.instance
    }

    @Provides
    @Singleton
    fun provideAccountService(retrofit: Retrofit): AccountService {
        return retrofit.create(AccountService::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryService(retrofit: Retrofit): CategoryService {
        return retrofit.create(CategoryService::class.java)
    }

    @Provides
    @Singleton
    fun provideTransactionService(retrofit: Retrofit): TransactionService {
        return retrofit.create(TransactionService::class.java)
    }
}
