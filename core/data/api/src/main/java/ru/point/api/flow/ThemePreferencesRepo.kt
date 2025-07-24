package ru.point.api.flow

import kotlinx.coroutines.flow.Flow

interface ThemePreferencesRepo {
    val isDarkThemeFlow: Flow<Boolean>

    val primaryColorNameFlow: Flow<String>

    suspend fun saveDarkTheme(isDark: Boolean)

    suspend fun savePrimaryColor(name: String)
}