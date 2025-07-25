package ru.point.settings.di

import ru.point.api.flow.AccountPreferencesRepo
import ru.point.api.flow.ThemePreferencesRepo
import ru.point.api.repository.SettingsRepository

interface SettingsDeps {
    val settingsRepository: SettingsRepository
    val accountPreferencesRepo: AccountPreferencesRepo
    val themePreferencesRepo: ThemePreferencesRepo
}
