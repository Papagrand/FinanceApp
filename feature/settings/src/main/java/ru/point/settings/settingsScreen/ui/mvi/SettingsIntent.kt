package ru.point.settings.settingsScreen.ui.mvi

internal sealed interface SettingsIntent {
    data object ToggleDarkTheme : SettingsIntent
}