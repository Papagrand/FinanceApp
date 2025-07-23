package ru.point.api.model

import java.math.BigDecimal

data class CategorySummary(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val totalAmount: BigDecimal,
    val percentage: BigDecimal = 0.0.toBigDecimal(),
)