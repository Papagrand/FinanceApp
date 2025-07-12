package ru.point.transactions.addOrEditTransaction.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.point.api.model.TransactionDto
import ru.point.api.repository.TransactionRepository
import ru.point.utils.common.Result
import javax.inject.Inject

class GetTransactionInfoUseCase @Inject constructor(
    private val repo: TransactionRepository,
) {
    operator fun invoke(transactionId: Int): Flow<Result<TransactionDto>> {
        return repo.getTransactionById(transactionId)
    }
}
