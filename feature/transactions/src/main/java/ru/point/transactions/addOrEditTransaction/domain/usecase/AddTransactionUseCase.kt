package ru.point.transactions.addOrEditTransaction.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.point.api.model.CreateTransactionResponseDto
import ru.point.api.repository.TransactionRepository
import ru.point.utils.common.Result
import javax.inject.Inject

internal class AddTransactionUseCase @Inject constructor(
    private val repo: TransactionRepository,
) {
    operator fun invoke(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
    ): Flow<Result<CreateTransactionResponseDto>> =
        repo.createTransaction(
            accountId,
            categoryId,
            amount,
            transactionDate,
            comment,
        )
}
