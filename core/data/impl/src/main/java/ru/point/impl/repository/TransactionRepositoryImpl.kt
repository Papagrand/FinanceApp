package ru.point.impl.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.api.model.TransactionDto
import ru.point.api.repository.TransactionRepository
import ru.point.impl.flow.safeApiFlow
import ru.point.impl.model.Transaction
import ru.point.impl.model.toDomain
import ru.point.impl.service.TransactionService
import ru.point.utils.common.Result
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
    override fun observePeriod(
        accountId: Int,
        startDateIso: String,
        endDateIso: String,
    ) = safeApiFlow {
        api.getByAccountForPeriod(accountId, startDateIso, endDateIso)
    }.mapResultList()

    private fun Flow<Result<List<Transaction>>>.mapResultList(): Flow<Result<List<TransactionDto>>> =
        map { result ->
            when (result) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(result.cause)
                is Result.Success -> Result.Success(result.data.map { it.toDomain() })
            }
        }
}
