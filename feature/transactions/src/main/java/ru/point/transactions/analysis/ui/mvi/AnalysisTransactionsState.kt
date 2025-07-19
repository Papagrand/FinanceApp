package ru.point.transactions.analysis.ui.mvi

import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import ru.point.transactions.analysis.domain.model.CategorySummary


internal data class AnalysisTransactionsState(
    val isIncome: Boolean = false,
    val isLoading: Boolean = false,
    val startDate: String = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE),
    val endDate: String = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
    val grandAmountSummary: BigDecimal = BigDecimal.ZERO,
    val categorySummaries: List<CategorySummary> = emptyList(),
    val error: String? = null,
    val noInternet: Boolean = false,
)
