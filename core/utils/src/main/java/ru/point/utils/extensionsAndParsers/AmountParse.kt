package ru.point.utils.extensionsAndParsers

import java.text.NumberFormat
import java.util.Locale

fun String.moneyToLong(): Long = filter { it.isDigit() }.toLong()

fun Long.formatMoney(currency: String = "₽"): String {
    val nf =
        NumberFormat.getInstance(Locale("ru")).apply {
            isGroupingUsed = true
            minimumFractionDigits = 0
            maximumFractionDigits = 0
        }
    return "${nf.format(this)} $currency"
}
