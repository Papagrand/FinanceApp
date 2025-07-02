package ru.point.transactions.expenses.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.api.model.TodayTransactions
import ru.point.api.repository.TransactionRepository
import ru.point.transactions.history.domain.usecase.sumAmounts
import ru.point.utils.common.Result

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
