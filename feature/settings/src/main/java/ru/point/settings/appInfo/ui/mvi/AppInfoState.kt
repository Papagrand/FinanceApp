package ru.point.settings.appInfo.ui.mvi

internal data class AppInfoState(
    val appVersion: String = "",
    val lastTimeUpdate: String = ""
)