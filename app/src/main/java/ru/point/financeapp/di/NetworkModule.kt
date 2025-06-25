package ru.point.financeapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.point.account.data.api.AccountService
import ru.point.core.utils.NetworkTracker
import ru.point.network.client.RetrofitProvider
import javax.inject.Singleton

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
}
