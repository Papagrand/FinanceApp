package ru.point.financeapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.point.financeapp.App
import ru.point.financeapp.MainActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        ViewModelModule::class,
        CoreModule::class
    ]
)
interface AppComponent {
    fun inject(app: App)
    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}