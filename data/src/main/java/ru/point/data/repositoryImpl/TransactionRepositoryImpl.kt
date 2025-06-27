package ru.point.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.core.common.Result
import ru.point.data.api.TransactionService
import ru.point.domain.model.Transaction
import ru.point.domain.repository.TransactionRepository
import ru.point.network.flow.safeApiFlow
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
    override fun observeToday(accountId: Int): Flow<Result<List<Transaction>>> {
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
                            Transaction(
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
    ): Flow<Result<List<Transaction>>> {
        return safeApiFlow {
            api.getByAccountForPeriod(accountId, startDateIso, endDateIso)
        }.map { result ->
            when (result) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(result.cause)
                is Result.Success ->
                    Result.Success(
                        result.data.map { dto ->
                            Transaction(
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
