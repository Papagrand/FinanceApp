package ru.point.api.model

import java.math.BigDecimal

data class TodayTransactions(
    val list: List<TransactionDto>,
    val total: BigDecimal,
)
