package ru.point.transactions.addOrEditTransaction.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.point.api.repository.TransactionRepository
import ru.point.utils.common.Result
import javax.inject.Inject

internal class DeleteTransactionUseCase @Inject constructor(
    private val repo: TransactionRepository,
) {
    operator fun invoke(transactionId: Int): Flow<Result<Unit>> = repo.deleteTransaction(transactionId)
}
