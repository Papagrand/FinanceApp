package ru.point.api.model

import java.math.BigDecimal
import java.time.LocalDate

data class ChartEntry(
    val date: LocalDate,
    val totalAmount: BigDecimal,
    val whatsMore: Int,
)