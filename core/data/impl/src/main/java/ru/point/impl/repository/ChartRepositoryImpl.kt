package ru.point.impl.repository

import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.point.api.model.ChartEntry
import ru.point.api.repository.ChartRepository
import ru.point.impl.model.Transaction
import ru.point.impl.service.TransactionService
import ru.point.utils.common.Result
import ru.point.utils.model.toAppError
import ru.point.utils.network.NetworkTracker

class ChartRepositoryImpl @Inject constructor(
    private val api: TransactionService,
    private val networkTracker: NetworkTracker,
    private val io: CoroutineDispatcher = Dispatchers.IO,
) : ChartRepository {

    override fun dayDiff(accountId: Int): Flow<Result<List<ChartEntry>>> = flow {
        emit(Result.Loading)

        if (!networkTracker.online.first()) {
            emit(Result.Success(emptyList()))
            return@flow
        }

        val to = LocalDate.now()
        val from = to.minusDays(29)

        val raw = withContext(io) {
            api.getByAccountForPeriod(accountId, from.toString(), to.toString())
        }

        val perDay: Map<LocalDate, List<Transaction>> = raw
            .groupBy { LocalDate.parse(it.transactionDate.take(10)) }

        val allDays: List<LocalDate> = (0..29)
            .map { to.minusDays(it.toLong()) }
            .reversed()

        val entries: List<ChartEntry> = allDays.map { day ->
            val dayTransactions = perDay[day].orEmpty()

            val incomes  = dayTransactions
                .filter { it.category.isIncome }
                .sumOf  { it.amount.toBigDecimal() }

            val expenses = dayTransactions
                .filter { !it.category.isIncome }
                .sumOf  { it.amount.toBigDecimal() }

            val diff = incomes - expenses

            ChartEntry(date = day, diff = diff)
        }

        emit(Result.Success(entries))
    }
        .catch { e -> emit(Result.Error(e.toAppError())) }
        .flowOn(io)
}