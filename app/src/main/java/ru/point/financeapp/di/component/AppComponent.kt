package ru.point.financeapp.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.point.account.di.deps.AccountDeps
import ru.point.categories.di.deps.CategoriesDeps
import ru.point.financeapp.App
import ru.point.financeapp.di.modules.CoreModule
import ru.point.impl.di.NetworkModule
import ru.point.impl.di.RepositoryModule
import ru.point.transactions.di.TransactionDeps
import javax.inject.Singleton
import ru.point.financeapp.MainActivity
import ru.point.impl.di.DatabaseModule
import ru.point.impl.di.WorkerModule
import ru.point.impl.work.DaggerWorkerFactory
import ru.point.settings.di.SettingsDeps
import ru.point.utils.network.NetworkTracker

/**
 * AppComponent — Корневой компонент Dagger, объединяющий все модули приложения:
 *  - NetworkModule — провайдер сетевых зависимостей (Retrofit, сервисы);
 *  - RepositoryModule — биндинг репозиториев к их интерфейсам;
 *  - ViewModelModule — multibinding ViewModel через ViewModelFactory;
 *  - CoreModule — провайдер основных утилитарных классов (AccountPreferences).
 *
 * При инициализации собирает граф зависимостей и умеет инжектить их в Application и MainActivity.
 */

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        WorkerModule::class,
        CoreModule::class,
        ViewModelModule::class
    ],
)
interface AppComponent : AccountDeps, CategoriesDeps, TransactionDeps, SettingsDeps {
    fun workerFactory(): DaggerWorkerFactory

    fun inject(app: App)

    fun inject(activity: MainActivity)

    fun networkTracker(): NetworkTracker

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(ctx: Context): Builder

        fun build(): AppComponent
    }
}
