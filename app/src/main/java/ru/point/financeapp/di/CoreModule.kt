package ru.point.financeapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.point.impl.di.ApplicationContext
import ru.point.utils.common.AccountPreferences
import javax.inject.Singleton

/**
 * CoreModule.
 *
 * Предоставляет:
 *  - AccountPreferences — класс, оборачивающий DataStore/SharedPreferences для хранения ID аккаунта,
 *    чтобы ViewModel’и и юзкейсы могли читать/писать его через DI.
 */

@Module
object CoreModule {
    @Provides
    @Singleton
    fun provideAccountPreferences(
        @ApplicationContext
        context: Context,
    ): AccountPreferences = AccountPreferences(context)
}
