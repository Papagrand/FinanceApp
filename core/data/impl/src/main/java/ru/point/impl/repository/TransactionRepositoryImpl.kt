package ru.point.impl.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.api.model.CreateTransactionResponseDto
import ru.point.api.model.TransactionDto
import ru.point.api.repository.TransactionRepository
import ru.point.impl.flow.safeApiFlow
import ru.point.impl.model.CreateTransactionRequest
import ru.point.impl.model.Transaction
import ru.point.impl.model.toDomain
import ru.point.impl.service.TransactionService
import ru.point.utils.common.Result
import ru.point.utils.common.Result.Error
import ru.point.utils.common.Result.Loading
import ru.point.utils.common.Result.Success
import javax.inject.Inject

/**
 * TransactionRepositoryImpl
 *
 * Ответственность:
 * - запрашивать транзакции из API через Retrofit;
 * - преобразовывать DTO в доменную модель Transaction;
 * - оборачивать результаты в Flow<Result<List<Transaction>>>.
 *
 */

class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionService,
) : TransactionRepository {
    override fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
    ): Flow<Result<CreateTransactionResponseDto>> =
        safeApiFlow {
            val requestBody =
                CreateTransactionRequest(
                    accountId = accountId,
                    categoryId = categoryId,
                    amount = amount,
                    transactionDate = transactionDate,
                    comment = comment,
                )
            api.createNewTransaction(requestBody)
        }
            .map { result ->
                when (result) {
                    is Loading -> Loading
                    is Error -> Error(result.cause)
                    is Success ->
                        Success(
                            data =
                                CreateTransactionResponseDto(
                                    id = result.data.id,
                                    accountId = result.data.accountId,
                                    categoryId = result.data.categoryId,
                                    amount = result.data.amount,
                                    transactionDate = result.data.transactionDate,
                                    comment = result.data.comment,
                                    createdAt = result.data.createdAt,
                                    updatedAt = result.data.updatedAt,
                                ),
                        )
                }
            }

    override fun deleteTransaction(transactionId: Int): Flow<Result<Unit>> =
        safeApiFlow {
            api.deleteTransaction(transactionId)
        }
            .map { result ->
                when (result) {
                    is Loading -> Loading
                    is Error -> Error(result.cause)
                    is Success ->
                        Success(
                            Unit,
                        )
                }
            }

    override fun observePeriod(
        accountId: Int,
        startDateIso: String,
        endDateIso: String,
    ) = safeApiFlow {
        api.getByAccountForPeriod(accountId, startDateIso, endDateIso)
    }.mapResultList()

    override fun getTransactionById(transactionId: Int): Flow<Result<TransactionDto>> =
        safeApiFlow {
            api.getTransactionById(transactionId)
        }.map { result ->
            when (result) {
                is Loading -> Loading
                is Error -> Error(result.cause)
                is Success -> {
                    Success(
                        result.data.toDomain(),
                    )
                }
            }
        }

    override fun updateTransaction(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
    ): Flow<Result<TransactionDto>> =
        safeApiFlow {
            val requestBody =
                CreateTransactionRequest(
                    accountId = accountId,
                    categoryId = categoryId,
                    amount = amount,
                    transactionDate = transactionDate,
                    comment = comment,
                )
            api.updateTransaction(transactionId, requestBody)
        }
            .map { result ->
                when (result) {
                    is Loading -> Loading
                    is Error -> Error(result.cause)
                    is Success -> {
                        Success(
                            result.data.toDomain(),
                        )
                    }
                }
            }

    private fun Flow<Result<List<Transaction>>>.mapResultList(): Flow<Result<List<TransactionDto>>> =
        map { result ->
            when (result) {
                is Loading -> Loading
                is Error -> Error(result.cause)
                is Success -> Success(result.data.map { it.toDomain() })
            }
        }
}
