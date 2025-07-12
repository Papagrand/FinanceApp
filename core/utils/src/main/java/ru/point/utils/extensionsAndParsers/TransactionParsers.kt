package ru.point.utils.extensionsAndParsers

import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun String.sanitizeDecimalInput(): String {
    if (isEmpty()) return ""
    val normalized = replace(',', '.')
    val cleaned = normalized.extractDecimalDigits()
    return cleaned.normalizeLeadingZeros()
}

private fun String.extractDecimalDigits(): String {
    val sb = StringBuilder()
    var dotFound = false
    var decimals = 0

    for (ch in this) {
        when {
            ch.isDigit() -> {
                if (!dotFound) {
                    sb.append(ch)
                } else if (decimals < 2) {
                    sb.append(ch)
                    decimals++
                }
            }

            ch == '.' && !dotFound -> {
                dotFound = true
                if (sb.isEmpty()) sb.append('0')
                sb.append('.')
            }
        }
    }

    return sb.toString()
}

private fun String.normalizeLeadingZeros(): String {
    val parts = split('.', limit = 2)
    val intPart = parts[0].trimStart('0').ifEmpty { "0" }

    return if (parts.size == 2) {
        "$intPart.${parts[1]}"
    } else {
        intPart
    }
}

private val ru = Locale("ru", "RU")
private val twoDecimals =
    NumberFormat.getNumberInstance(ru).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
        isGroupingUsed = true
    }
private val optional =
    NumberFormat.getNumberInstance(ru).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 2
        isGroupingUsed = true
    }

fun formatForDisplay(
    raw: String,
    alwaysShowDecimals: Boolean = false,
): String {
    val fmt = if (alwaysShowDecimals) twoDecimals else optional
    return raw.toDoubleOrNull()?.let(fmt::format) ?: raw
}

fun buildIsoInstantString(
    date: String,
    time: String,
    zone: ZoneId = ZoneOffset.UTC,
): String? =
    runCatching {
        val localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        val localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        val dt =
            LocalDateTime.of(localDate, localTime)
                .atZone(zone)
                .toInstant()
                .truncatedTo(ChronoUnit.MILLIS)
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneOffset.UTC)
            .format(dt)
    }.getOrNull()
