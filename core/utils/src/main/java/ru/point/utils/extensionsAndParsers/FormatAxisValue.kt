package ru.point.utils.extensionsAndParsers

import java.math.BigDecimal
import java.math.RoundingMode


fun formatAxisValue(value: BigDecimal): String {
    val intPart = value.setScale(0, RoundingMode.DOWN).abs().toPlainString()
    val digits = intPart.length

    return when {
        digits <= 4 ->
            intPart

        digits <= 6 -> {
            val thousands = value
                .divide(BigDecimal(1_000), 1, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()
            "${thousands}т"
        }

        digits <= 9 -> {
            val millions = value
                .divide(BigDecimal(1_000_000), 1, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()
            "${millions}мл"
        }

        digits <= 12 -> {
            val billions = value
                .divide(BigDecimal(1_000_000_000), 1, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()
            "${billions}млр"
        }

        digits > 12 -> {
            val billions = value
                .divide(BigDecimal(1_000_000_000_000), 1, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()
            "${billions}тр"
        }

        else ->
            value
                .setScale(1, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toEngineeringString()
    }
}
