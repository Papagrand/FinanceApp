package ru.point.settings.di

import kotlin.properties.Delegates.notNull

object SettingsDepsStore : SettingsDepsProvider {
    override var settingsDeps: SettingsDeps by notNull()
}
