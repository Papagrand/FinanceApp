package ru.point.api.repository

interface SettingsRepository {

    fun getAppVersion(): String

    fun getAppLastTimeUpdate(): String

}