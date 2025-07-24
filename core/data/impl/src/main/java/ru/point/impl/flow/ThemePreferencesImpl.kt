package ru.point.impl.flow

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.api.flow.ThemePreferencesRepo

private val Context.themeDataStore by preferencesDataStore(name = "theme_prefs")

private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

private val PRIMARY_COLOR_KEY  = stringPreferencesKey("primary_color")

class ThemePreferencesImpl @Inject constructor(
    private val context: Context,
) : ThemePreferencesRepo {

    override val isDarkThemeFlow: Flow<Boolean> =
        context.themeDataStore.data
            .map { prefs -> prefs[DARK_MODE_KEY] ?: false }

    override suspend fun saveDarkTheme(isDark: Boolean) {
        context.themeDataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = isDark
        }
    }

    override val primaryColorNameFlow: Flow<String> =
        context.themeDataStore.data
            .map { it[PRIMARY_COLOR_KEY] ?: "green" }

    override suspend fun savePrimaryColor(name: String) {
        context.themeDataStore.edit { it[PRIMARY_COLOR_KEY] = name }
    }
}