package ru.point.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.core.common.Result
import ru.point.domain.model.TodayTransactions
import ru.point.domain.model.Transaction
import ru.point.domain.repository.TransactionRepository
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * GetExpensesTodayUseCase GetIncomesTodayUseCase
 *
 * Ответственность:
 * - фильтровать поток транзакций по расходам или расходам;
 * - вычислять общую сумму расходов и упаковывать в TodayTransactions.
 *
 */

class GetExpensesTodayUseCase(private val repo: TransactionRepository) {
    operator fun invoke(id: Int): Flow<Result<TodayTransactions>> =
        repo.observeToday(id)
            .map { result ->
                when (result) {
                    is Result.Success ->
                        Result.Success(
                            TodayTransactions(
                                list = result.data.filter { !it.isIncome },
                                total =
                                    result.data
                                        .filter { !it.isIncome }
                                        .sumAmounts(),
                            ),
                        )

                    is Result.Loading -> Result.Loading
                    is Result.Error -> Result.Error(result.cause)
                }
            }
}

class GetIncomesTodayUseCase(private val repo: TransactionRepository) {
    operator fun invoke(id: Int): Flow<Result<TodayTransactions>> =
        repo.observeToday(id)
            .map { result ->
                when (result) {
                    is Result.Success ->
                        Result.Success(
                            TodayTransactions(
                                list = result.data.filter { it.isIncome },
                                total =
                                    result.data
                                        .filter { it.isIncome }
                                        .sumAmounts(),
                            ),
                        )

                    is Result.Loading -> Result.Loading
                    is Result.Error -> Result.Error(result.cause)
                }
            }
}

/**
 * GetTransactionHistoryUseCase
 *
 * Ответственность:
 * - получать транзакции за текущий месяц, фильтровать по типу (доход/расход);
 * - сортировать по дате и суммировать их;
 * - упаковывать результат в TodayTransactions.
 *
 */

class GetTransactionHistoryUseCase(
    private val repo: TransactionRepository
) {

    operator fun invoke(
        accountId: Int,
        isIncome: Boolean
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
        result: Result<List<Transaction>>,
        isIncome: Boolean
    ): Result<TodayTransactions> = when (result) {
        is Result.Loading -> Result.Loading
        is Result.Error -> Result.Error(result.cause)
        is Result.Success -> {
            val filtered = filterAndSort(result.data, isIncome)
            Result.Success(
                TodayTransactions(
                    list = filtered,
                    total = filtered.sumAmounts()
                )
            )
        }
    }

    private fun filterAndSort(
        list: List<Transaction>,
        isIncome: Boolean
    ): List<Transaction> {
        return list
            .filter { it.isIncome == isIncome }
            .sortedByDescending { dto ->
                Instant.parse(dto.dateTime)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            }
    }
}

fun List<Transaction>.sumAmounts(): BigDecimal = fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount.toBigDecimal() }
