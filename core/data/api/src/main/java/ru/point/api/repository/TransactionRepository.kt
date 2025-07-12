package ru.point.api.repository

import kotlinx.coroutines.flow.Flow
import ru.point.api.model.CreateTransactionResponseDto
import ru.point.api.model.TransactionDto
import ru.point.utils.common.Result

/**
 * TransactionRepository
 *
 * Ответственность:
 * - предоставлять потоки транзакций за сегодня и за заданный период.
 */

interface TransactionRepository {
    fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
    ): Flow<Result<CreateTransactionResponseDto>>

    fun deleteTransaction(transactionId: Int): Flow<Result<Unit>>

    fun observePeriod(
        accountId: Int,
        startDateIso: String,
        endDateIso: String,
    ): Flow<Result<List<TransactionDto>>>

    fun getTransactionById(transactionId: Int): Flow<Result<TransactionDto>>

    fun updateTransaction(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
    ): Flow<Result<TransactionDto>>
}
