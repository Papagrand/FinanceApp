package ru.point.impl.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.api.model.TransactionDto
import ru.point.api.repository.TransactionRepository
import ru.point.impl.flow.safeApiFlow
import ru.point.impl.service.TransactionService
import ru.point.utils.common.Result
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
    override fun observeToday(accountId: Int): Flow<Result<List<TransactionDto>>> {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

        return safeApiFlow {
            api.getByAccountForPeriod(accountId, today, today)
        }.map { result ->
            when (result) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(result.cause)
                is Result.Success ->
                    Result.Success(
                        result.data.map { dto ->
                            TransactionDto(
                                id = dto.id,
                                accountId = dto.account.id,
                                accountName = dto.account.name,
                                amount = dto.amount,
                                currency = dto.account.currency,
                                categoryId = dto.category.id,
                                categoryName = dto.category.name,
                                emoji = dto.category.emoji,
                                isIncome = dto.category.isIncome,
                                dateTime = dto.transactionDate,
                                comment = dto.comment,
                                totalAmount = dto.account.balance,
                            )
                        },
                    )
            }
        }
    }

    override fun observePeriod(
        accountId: Int,
        startDateIso: String,
        endDateIso: String,
    ): Flow<Result<List<TransactionDto>>> {
        return safeApiFlow {
            api.getByAccountForPeriod(accountId, startDateIso, endDateIso)
        }.map { result ->
            when (result) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(result.cause)
                is Result.Success ->
                    Result.Success(
                        result.data.map { dto ->
                            TransactionDto(
                                id = dto.id,
                                accountId = dto.account.id,
                                accountName = dto.account.name,
                                amount = dto.amount,
                                currency = dto.account.currency,
                                categoryId = dto.category.id,
                                categoryName = dto.category.name,
                                emoji = dto.category.emoji,
                                isIncome = dto.category.isIncome,
                                dateTime = dto.transactionDate,
                                comment = dto.comment,
                                totalAmount = dto.account.balance,
                            )
                        },
                    )
            }
        }
    }
}
