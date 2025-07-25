package ru.point.settings.mainColorScreen.di

import dagger.Component
import ru.point.settings.di.SettingsDeps
import ru.point.settings.mainColorScreen.ui.mvi.MainColorViewModelFactory

@Component(dependencies = [SettingsDeps::class])
internal interface MainColorComponent {
    @Component.Builder
    interface Builder {
        fun deps(settingsDeps: SettingsDeps): Builder

        fun build(): MainColorComponent
    }

    val mainColorViewModelFactory: MainColorViewModelFactory
}