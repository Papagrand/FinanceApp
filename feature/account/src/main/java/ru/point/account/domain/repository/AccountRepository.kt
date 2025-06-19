package ru.point.account.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.point.account.domain.model.Account
import ru.point.core.common.Result

interface AccountRepository {
    fun observe(): Flow<Result<List<Account>>>
}