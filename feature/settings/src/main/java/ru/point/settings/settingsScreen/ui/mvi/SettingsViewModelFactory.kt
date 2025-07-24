package ru.point.settings.settingsScreen.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import ru.point.api.flow.ThemePreferencesRepo

internal class SettingsViewModelFactory @Inject constructor(
    private val themeRepo: ThemePreferencesRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SettingsViewModel(themeRepo) as T
    }
}