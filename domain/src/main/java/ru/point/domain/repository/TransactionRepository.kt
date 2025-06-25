package ru.point.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.point.domain.model.Transaction
import ru.point.core.common.Result

/**
 * TransactionRepository
 *
 * Ответственность:
 * - предоставлять потоки транзакций за сегодня и за заданный период.
 */

interface TransactionRepository {
    fun observeToday(accountId: Int): Flow<Result<List<Transaction>>>

    fun observePeriod(
        accountId: Int,
        startDateIso: String,
        endDateIso: String
    ): Flow<Result<List<Transaction>>>
}