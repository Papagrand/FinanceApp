package ru.point.financeapp.di.modules

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.point.api.flow.AccountPreferencesRepo
import ru.point.impl.flow.AccountPreferencesImpl
import javax.inject.Singleton

/**
 * CoreModule.
 *
 * Предоставляет:
 *  - AccountPreferences — класс, оборачивающий DataStore/SharedPreferences для хранения ID аккаунта,
 *    чтобы ViewModel’и и юзкейсы могли читать/писать его через DI.
 */

@Module(includes = [PrefsModule::class])
object CoreModule {
    @Provides
    @Singleton
    fun provideAccountPreferences(context: Context): AccountPreferencesImpl = AccountPreferencesImpl(context)
}

@Module
interface PrefsModule {
    @Binds
    fun bindAccountPreferences(impl: AccountPreferencesImpl): AccountPreferencesRepo
}
