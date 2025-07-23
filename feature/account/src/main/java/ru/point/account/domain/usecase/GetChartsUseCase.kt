package ru.point.account.domain.usecase

import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.api.model.ChartEntry
import ru.point.api.model.TransactionDto
import ru.point.api.repository.TransactionRepository
import ru.point.utils.common.Result

internal class GetChartsUseCase @Inject constructor(
    private val repo: TransactionRepository
) {

    operator fun invoke(accountId: Int): Flow<Result<List<ChartEntry>>> {
        val end = LocalDate.now()
        val start = end.minusDays(29)

        return repo.observePeriod(
            accountId = accountId,
            startDateIso = start.toString(),
            endDateIso = end.toString(),
        ).map { result ->
            when (result) {
                is Result.Loading -> Result.Loading
                is Result.Error -> result
                is Result.Success -> Result.Success(
                    result.data.toChartEntries(start, end)
                )
            }
        }
    }
}

private fun List<TransactionDto>.toChartEntries(
    start: LocalDate,
    end: LocalDate
): List<ChartEntry> {

    val perDay: Map<LocalDate, List<TransactionDto>> = groupBy { dto ->
        LocalDate.parse(dto.dateTime.take(10))
    }

    // Формируем полный список дней периода (включая start и end)
    val allDays: List<LocalDate> = generateSequence(start) { prev ->
        val next = prev.plusDays(1)
        if (next <= end) next else null
    }.toList()

    return allDays.map { day ->
        val dayTransactions = perDay[day].orEmpty()

        val incomes = dayTransactions
            .filter { it.isIncome }
            .sumOf { it.amount.toBigDecimal() }

        val expenses = dayTransactions
            .filter { !it.isIncome }
            .sumOf { it.amount.toBigDecimal() }

        ChartEntry(date = day, diff = incomes - expenses)
    }
}