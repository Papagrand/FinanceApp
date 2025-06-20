package ru.point.expenses.presentation.mvi.expenses

import ru.point.domain.model.Transaction
import java.math.BigDecimal

data class ExpensesState(
    val isLoading: Boolean = false,
    val list: List<Transaction> = emptyList(),
    val total: BigDecimal = BigDecimal.ZERO,
    val error: String? = null
)