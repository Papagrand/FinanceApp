package ru.point.core.utils

enum class CurrencyParse(val code: String, val symbol: String) {
    RUB("RUB", "₽"),
    EUR("EUR", "€"),
    USD("USD", "$"),
    ;

    companion object {
        fun from(raw: String): CurrencyParse? = values().firstOrNull { it.code.equals(raw.trim(), ignoreCase = true) }
    }
}
