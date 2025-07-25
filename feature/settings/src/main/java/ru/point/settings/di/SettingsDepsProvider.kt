package ru.point.settings.di

interface SettingsDepsProvider {
    val settingsDeps: SettingsDeps

    companion object : SettingsDepsProvider by SettingsDepsStore
}
