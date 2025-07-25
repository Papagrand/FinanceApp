package ru.point.transactions.analysis.domain.model

import java.math.BigDecimal
import ru.point.api.model.CategorySummary

data class AnalysisTransactions(
    val categorySummaries: List<CategorySummary>,
    val grandTotalAmount: BigDecimal,
)
