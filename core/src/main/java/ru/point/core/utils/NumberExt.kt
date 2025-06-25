package ru.point.core.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun String.toPrettyNumber(): String {
    val value = toBigDecimalOrNull() ?: return this

    val symbols =
        DecimalFormatSymbols(Locale("ru", "RU")).apply {
            groupingSeparator = '\u202F'
        }

    val pattern = "#,##0.##"
    return DecimalFormat(pattern, symbols).format(value)
}

fun String.toCurrencySymbol(): String = CurrencyParse.from(this)?.symbol ?: this
