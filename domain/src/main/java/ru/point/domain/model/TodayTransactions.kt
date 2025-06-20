package ru.point.domain.model

import java.math.BigDecimal

data class TodayTransactions(
    val list: List<Transaction>,
    val total: BigDecimal
)