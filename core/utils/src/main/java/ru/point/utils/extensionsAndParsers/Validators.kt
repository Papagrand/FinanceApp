package ru.point.utils.extensionsAndParsers

private val BALANCE_RE = Regex("""^-?\d{1,13}(\.\d{0,2})?$""")

fun String.validateBalance(): String? {
    if (isBlank()) return null
    if (!BALANCE_RE.matches(this)) return null

    // отделяем возможный минус от «целой» части
    val sign = if (startsWith('-')) "-" else ""
    val numeric = removePrefix("-")

    val parts = numeric.split('.')
    val normalized =
        when (parts.size) {
            1 -> "${parts[0]}.00"
            2 -> "${parts[0]}.${parts[1].padEnd(2, '0').take(2)}"
            else -> return null
        }

    return sign + normalized
}
