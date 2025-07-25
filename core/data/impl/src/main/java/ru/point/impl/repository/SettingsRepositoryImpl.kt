package ru.point.impl.repository

import android.content.Context
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import ru.point.api.repository.SettingsRepository
import ru.point.utils.extensionsAndParsers.getAppVersionName

class SettingsRepositoryImpl @Inject constructor(private val context: Context) : SettingsRepository {

    override fun getAppVersion() = context.getAppVersionName()

    override fun getAppLastTimeUpdate(): String {
        val millis = context.packageManager
            .getPackageInfo(context.packageName, 0).lastUpdateTime

        val fullDateFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        val formatter = fullDateFormatter ?: DateTimeFormatter.ofPattern("dd.MM.yyyy")

        val localDate = Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        return formatter.format(localDate)
    }
}