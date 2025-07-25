package ru.point.settings.settingsScreen.di

import dagger.Component
import ru.point.settings.di.SettingsDeps
import ru.point.settings.settingsScreen.ui.mvi.SettingsViewModelFactory

@Component(dependencies = [SettingsDeps::class])
internal interface SettingsComponent {
    @Component.Builder
    interface Builder {
        fun deps(settingsDeps: SettingsDeps): Builder

        fun build(): SettingsComponent
    }

    val settingsViewModelFactory: SettingsViewModelFactory
}