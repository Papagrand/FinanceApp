package ru.point.impl.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.point.impl.service.AccountService
import ru.point.impl.service.CategoryService
import ru.point.impl.service.TransactionService
import ru.point.utils.network.ApiKeyInterceptor
import ru.point.utils.network.NetworkTracker
import ru.point.utils.network.RetryInterceptor
import javax.inject.Singleton

/**
 * NetworkModule
 *
 * Предоставляет:
 *  - NetworkTracker — подписка на изменения сети;
 *  - Retrofit — настроенный клиент для REST API;
 *  - AccountService — API для работы с аккаунтами;
 *  - CategoryService — API для работы с категориями;
 *  - TransactionService — API для работы с транзакциями.
 */

@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkTracker(
        @ApplicationContext
        context: Context,
    ): NetworkTracker {
        return NetworkTracker(context)
    }

    @Provides
    @Singleton
    fun provideRetrofit(tracker: NetworkTracker): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()
        val okHttp =
            OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor())
                .addInterceptor(RetryInterceptor(tracker))
                .build()

        return Retrofit.Builder()
            .baseUrl("https://shmr-finance.ru/api/v1/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttp)
            .build()
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
