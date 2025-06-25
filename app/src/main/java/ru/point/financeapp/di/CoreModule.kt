package ru.point.financeapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.point.core.common.AccountPreferences
import javax.inject.Singleton

@Module
object CoreModule {
    @Provides
    @Singleton
    fun provideAccountPreferences(context: Context): AccountPreferences = AccountPreferences(context)
}
