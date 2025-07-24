package ru.point.api.flow

import kotlinx.coroutines.flow.Flow

interface ThemePreferencesRepo {
    val isDarkThemeFlow: Flow<Boolean>

    suspend fun saveDarkTheme(isDark: Boolean)
}