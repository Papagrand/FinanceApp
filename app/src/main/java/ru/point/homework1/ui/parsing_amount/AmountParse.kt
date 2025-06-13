package ru.point.homework1.ui.parsing_amount


fun String.moneyToLong(): Long =
    filter { it.isDigit() }.toLong()

fun Long.formatMoney(currency: String = "₽"): String {
    val nf = java.text.NumberFormat.getInstance(java.util.Locale("ru")).apply {
        isGroupingUsed = true
        minimumFractionDigits = 0
        maximumFractionDigits = 0
    }
    return "${nf.format(this)} $currency"
}

