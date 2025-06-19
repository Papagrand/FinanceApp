package ru.point.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.domain.model.Transaction
import ru.point.domain.repository.TransactionRepository
import ru.point.core.common.Result
import ru.point.domain.model.TodayTransactions
import java.math.BigDecimal

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
                            list  = result.data.filter { it.isIncome },
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


inline fun <T> Result<List<T>>.filterSuccess(
    predicate: (T) -> Boolean
): Result<List<T>> = when (this) {
    is Result.Success -> Result.Success(data.filter(predicate))
    is Result.Loading,
    is Result.Error -> this
}

fun List<Transaction>.sumAmounts(): BigDecimal =
    fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount.toBigDecimal() }