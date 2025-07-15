package ru.point.transactions.analysis.domain.model

import java.math.BigDecimal

data class AnalysisTransactions(
    val categorySummaries: List<CategorySummary>,
    val grandTotalAmount: BigDecimal,
)
