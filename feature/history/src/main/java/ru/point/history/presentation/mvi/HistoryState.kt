package ru.point.history.presentation.mvi

import ru.point.domain.model.Transaction
import java.math.BigDecimal

data class HistoryState(
    val isLoading: Boolean = false,
    val list: List<Transaction> = emptyList(),
    val total: BigDecimal = BigDecimal.ZERO,
    val error: String? = null,
)
