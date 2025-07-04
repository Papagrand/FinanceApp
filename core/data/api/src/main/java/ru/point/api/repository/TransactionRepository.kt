package ru.point.api.repository

import kotlinx.coroutines.flow.Flow
import ru.point.api.model.TransactionDto
import ru.point.utils.common.Result

/**
 * TransactionRepository
 *
 * Ответственность:
 * - предоставлять потоки транзакций за сегодня и за заданный период.
 */

interface TransactionRepository {
    fun observePeriod(
        accountId: Int,
        startDateIso: String,
        endDateIso: String,
    ): Flow<Result<List<TransactionDto>>>
}
