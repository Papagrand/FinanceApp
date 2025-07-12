package ru.point.utils.extensionsAndParsers

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val DATE_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val TIME_FMT = DateTimeFormatter.ofPattern("HH:mm")

interface DateParcer {
    fun parseDate(
        isoUtc: String,
        zone: ZoneId = ZoneId.systemDefault(),
    ): String {
        val instant = Instant.parse(isoUtc)
        return instant.atZone(zone).format(DATE_FMT)
    }

    fun parseTime(
        isoUtc: String,
        zone: ZoneId = ZoneId.systemDefault(),
    ): String = Instant.parse(isoUtc).atZone(zone).format(TIME_FMT)
}
