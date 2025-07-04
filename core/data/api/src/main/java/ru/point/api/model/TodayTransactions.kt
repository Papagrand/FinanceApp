package ru.point.api.model

import java.math.BigDecimal

data class TodayTransactions(
    val transactionsList: List<TransactionDto>,
    val total: BigDecimal,
)
