package ru.point.account.domain.usecase

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
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
                    result.data.toChartEntries()
                )
            }
        }
    }
}

fun List<TransactionDto>.toChartEntries(): List<ChartEntry> {
    val endDate = LocalDate.now()
    val startDate = endDate.minusDays(29)
    val zone = ZoneId.systemDefault()

    val fullDates = (0L..29L).map { startDate.plusDays(it) }

    val byDate: Map<LocalDate, List<TransactionDto>> = this
        .mapNotNull { dto ->
            val txDate = Instant.parse(dto.dateTime)
                .atZone(zone)
                .toLocalDate()
            if (txDate in startDate..endDate) dto to txDate else null
        }
        .groupBy({ it.second }, { it.first })

    var nextDayBalance: BigDecimal = byDate[endDate]
        ?.maxByOrNull { it.dateTime }
        ?.totalAmount
        ?.toBigDecimal()
        ?: BigDecimal.ZERO

    val reversedResult = mutableListOf<ChartEntry>()

    for (date in fullDates.asReversed()) {
        val dayTx = byDate[date].orEmpty()

        val entryBalance = nextDayBalance

        val sumIn = dayTx.filter { it.isIncome }
            .sumOf { it.amount.toBigDecimal() }
        val sumOut = dayTx.filter { !it.isIncome }
            .sumOf { it.amount.toBigDecimal() }

        val whatsMore = when {
            sumIn > sumOut -> 1
            sumOut > sumIn -> 2
            else -> 0
        }

        reversedResult += ChartEntry(
            date = date,
            totalAmount = entryBalance,
            whatsMore = whatsMore
        )

        nextDayBalance = entryBalance - sumIn + sumOut
    }

    return reversedResult.asReversed()
}