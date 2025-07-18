package ru.point.impl.di

import android.content.Context
import android.net.ConnectivityManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.point.impl.service.AccountService
import ru.point.impl.service.CategoryService
import ru.point.impl.service.TransactionService
import ru.point.utils.network.ApiKeyInterceptor
import ru.point.utils.network.RetryInterceptor

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
    fun provideRetrofit(): Retrofit {
        val json =
            Json {
                ignoreUnknownKeys = true
                explicitNulls = true
            }
        val contentType = "application/json".toMediaType()
        val okHttp =
            OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor())
                .addInterceptor(RetryInterceptor())
                .build()

        @OptIn(ExperimentalSerializationApi::class)
        return Retrofit.Builder()
            .baseUrl("https://shmr-finance.ru/api/v1/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttp)
            .build()
    }

    @Provides @Singleton
    fun provideConnectivityManager(ctx: Context): ConnectivityManager =
        ctx.getSystemService(ConnectivityManager::class.java)

    @Provides
    @Singleton
    internal fun provideAccountService(retrofit: Retrofit): AccountService {
        return retrofit.create(AccountService::class.java)
    }

    @Provides
    @Singleton
    internal fun provideCategoryService(retrofit: Retrofit): CategoryService {
        return retrofit.create(CategoryService::class.java)
    }

    @Provides
    @Singleton
    internal fun provideTransactionService(retrofit: Retrofit): TransactionService {
        return retrofit.create(TransactionService::class.java)
    }
}
