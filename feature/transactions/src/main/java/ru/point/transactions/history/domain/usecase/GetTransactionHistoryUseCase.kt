package ru.point.transactions.history.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.api.model.TodayTransactions
import ru.point.api.model.TransactionDto
import ru.point.api.repository.TransactionRepository
import ru.point.utils.common.Result
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class GetTransactionHistoryUseCase(
    private val repo: TransactionRepository,
) {
    operator fun invoke(
        accountId: Int,
        isIncome: Boolean,
    ): Flow<Result<TodayTransactions>> {
        val (isoStart, isoEnd) = computeDateRange()
        return repo.observePeriod(accountId, isoStart, isoEnd)
            .map { result -> mapToTodayTransactions(result, isIncome) }
    }

    private fun computeDateRange(): Pair<String, String> {
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        val isoStart = startOfMonth.format(DateTimeFormatter.ISO_DATE)
        val isoEnd = today.format(DateTimeFormatter.ISO_DATE)
        return isoStart to isoEnd
    }

    private fun mapToTodayTransactions(
        result: Result<List<TransactionDto>>,
        isIncome: Boolean,
    ): Result<TodayTransactions> =
        when (result) {
            is Result.Loading -> Result.Loading
            is Result.Error -> Result.Error(result.cause)
            is Result.Success -> {
                val filtered = filterAndSort(result.data, isIncome)
                Result.Success(
                    TodayTransactions(
                        list = filtered,
                        total = filtered.sumAmounts(),
                    ),
                )
            }
        }

    private fun filterAndSort(
        list: List<TransactionDto>,
        isIncome: Boolean,
    ): List<TransactionDto> {
        return list
            .filter { it.isIncome == isIncome }
            .sortedByDescending { dto ->
                Instant.parse(dto.dateTime)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            }
    }
}

fun List<TransactionDto>.sumAmounts(): BigDecimal = fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount.toBigDecimal() }
