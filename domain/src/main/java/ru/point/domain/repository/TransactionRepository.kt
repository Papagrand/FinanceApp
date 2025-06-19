package ru.point.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.point.domain.model.Transaction
import ru.point.core.common.Result

interface TransactionRepository {
    fun observeToday(accountId: Int): Flow<Result<List<Transaction>>>
}