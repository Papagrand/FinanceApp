package ru.point.settings.appInfo.di

import dagger.Component
import ru.point.settings.appInfo.ui.mvi.AppInfoViewModelFactory
import ru.point.settings.di.SettingsDeps

@Component(dependencies = [SettingsDeps::class])
internal interface AppInfoComponent {
    @Component.Builder
    interface Builder {
        fun deps(settingsDeps: SettingsDeps): Builder

        fun build(): AppInfoComponent
    }

    val appInfoViewModelFactory: AppInfoViewModelFactory
}