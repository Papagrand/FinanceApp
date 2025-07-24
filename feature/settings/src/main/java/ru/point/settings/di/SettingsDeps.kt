package ru.point.settings.di

import ru.point.api.flow.AccountPreferencesRepo
import ru.point.api.flow.ThemePreferencesRepo

interface SettingsDeps {
    val accountPreferencesRepo: AccountPreferencesRepo
    val themePreferencesRepo: ThemePreferencesRepo
}
