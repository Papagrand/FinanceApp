package ru.point.transactions.history.ui.mvi

import ru.point.api.model.TransactionDto
import java.math.BigDecimal

internal data class HistoryState(
    val isLoading: Boolean = false,
    val list: List<TransactionDto> = emptyList(),
    val total: BigDecimal = BigDecimal.ZERO,
    val error: String? = null,
)
