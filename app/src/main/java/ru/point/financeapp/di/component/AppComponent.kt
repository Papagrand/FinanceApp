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
        RepositoryModule::class,
        CoreModule::class,
    ],
)
interface AppComponent : AccountDeps, CategoriesDeps, TransactionDeps {
    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(ctx: Context): Builder

        fun build(): AppComponent
    }
}
