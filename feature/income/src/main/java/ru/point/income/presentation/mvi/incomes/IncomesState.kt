package ru.point.income.presentation.mvi.incomes

import ru.point.domain.model.Transaction
import java.math.BigDecimal

data class IncomesState(
    val isLoading: Boolean = false,
    val list: List<Transaction> = emptyList(),
    val total: BigDecimal = BigDecimal.ZERO,
    val error: String? = null
)