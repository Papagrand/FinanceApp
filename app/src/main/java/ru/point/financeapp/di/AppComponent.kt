package ru.point.financeapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.point.financeapp.App
import ru.point.financeapp.MainActivity
import javax.inject.Singleton

/**
 * AppComponent — Корневой компонент Dagger, объединяющий все модули приложения:
 *  - NetworkModule — провайдер сетевых зависимостей (Retrofit, сервисы);
 *  - RepositoryModule — биндинг репозиториев к их интерфейсам;
 *  - UseCaseModule — провайдер бизнес-логики (use-case’ов);
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
        UseCaseModule::class,
        ViewModelModule::class,
        CoreModule::class,
    ],
)
interface AppComponent {
    fun inject(app: App)

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }
}
