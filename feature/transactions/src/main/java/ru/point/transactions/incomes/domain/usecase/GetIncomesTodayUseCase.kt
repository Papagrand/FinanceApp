package ru.point.transactions.incomes.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.point.api.flow.toTodayTransactions
import ru.point.api.model.TodayTransactions
import ru.point.api.repository.TransactionRepository
import ru.point.utils.common.Result
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetIncomesTodayUseCase @Inject constructor(
    private val repo: TransactionRepository,
) {
    operator fun invoke(accountId: Int): Flow<Result<TodayTransactions>> {
        val todayIso = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        return repo
            .observePeriod(accountId, todayIso, todayIso)
            .toTodayTransactions(isIncome = true)
    }
}
