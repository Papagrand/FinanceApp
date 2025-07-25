package ru.point.transactions.addOrEditTransaction.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.point.api.model.TransactionDto
import ru.point.api.repository.TransactionRepository
import ru.point.utils.common.Result
import javax.inject.Inject

internal class UpdateTransactionUseCase @Inject constructor(
    private val repo: TransactionRepository,
) {
    operator fun invoke(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?,
        isIncome: Boolean
    ): Flow<Result<TransactionDto>> {
        return repo.updateTransaction(
            transactionId,
            accountId,
            categoryId,
            amount,
            transactionDate,
            comment,
            isIncome
        )
    }
}
