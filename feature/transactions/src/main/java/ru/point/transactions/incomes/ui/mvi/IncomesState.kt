package ru.point.transactions.incomes.ui.mvi

import ru.point.api.model.TransactionDto
import java.math.BigDecimal

data class IncomesState(
    val isLoading: Boolean = false,
    val list: List<TransactionDto> = emptyList(),
    val total: BigDecimal = BigDecimal.ZERO,
    val error: String? = null,
)
