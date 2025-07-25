package ru.point.settings.appInfo.ui.mvi

internal sealed interface AppInfoIntent {
    data object LoadVersion : AppInfoIntent
    data object LoadLastUpdate : AppInfoIntent
}