package ru.point.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.domain.model.Transaction
import ru.point.domain.repository.TransactionRepository
import ru.point.core.common.Result
import ru.point.domain.model.TodayTransactions
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class GetExpensesTodayUseCase(private val repo: TransactionRepository) {
    operator fun invoke(id: Int): Flow<Result<TodayTransactions>> =
        repo.observeToday(id)
            .map { result ->
                when (result) {
                    is Result.Success -> Result.Success(
                        TodayTransactions(
                            list = result.data.filter { !it.isIncome },
                            total = result.data
                                .filter { !it.isIncome }
                                .sumAmounts()
                        )
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
                    is Result.Success -> Result.Success(
                        TodayTransactions(
                            list = result.data.filter { it.isIncome },
                            total = result.data
                                .filter { it.isIncome }
                                .sumAmounts()
                        )
                    )

                    is Result.Loading -> Result.Loading
                    is Result.Error -> Result.Error(result.cause)
                }
            }
}

class GetTransactionHistoryUseCase(
    private val repo: TransactionRepository,
    private val isIncome: Boolean
) {
    operator fun invoke(accountId: Int): Flow<Result<TodayTransactions>> {
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        val isoStart = startOfMonth.format(DateTimeFormatter.ISO_DATE)
        val isoEnd = today.format(DateTimeFormatter.ISO_DATE)

        return repo.observePeriod(accountId, isoStart, isoEnd)
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        val expenses = result.data
                            .filter {
                                if(isIncome){
                                    it.isIncome
                                }else{
                                    !it.isIncome
                                }
                            }
                            .sortedByDescending { dto ->
                                val instant = Instant.parse(dto.dateTime)
                                LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                            }

                        Result.Success(
                            TodayTransactions(
                                list = expenses,
                                total = expenses.sumAmounts()
                            )
                        )
                    }

                    is Result.Loading -> Result.Loading
                    is Result.Error -> Result.Error(result.cause)
                }
            }
    }
}


inline fun <T> Result<List<T>>.filterSuccess(
    predicate: (T) -> Boolean
): Result<List<T>> = when (this) {
    is Result.Success -> Result.Success(data.filter(predicate))
    is Result.Loading,
    is Result.Error -> this
}

fun List<Transaction>.sumAmounts(): BigDecimal =
    fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount.toBigDecimal() }