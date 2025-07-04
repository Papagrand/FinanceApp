package ru.point.api.repository

import kotlinx.coroutines.flow.Flow
import ru.point.api.model.AccountDto
import ru.point.utils.common.Result

interface AccountRepository {
    fun observe(): Flow<Result<AccountDto>>

    fun updateAccount(
        id: Int,
        name: String,
        balance: String,
        currency: String,
    ): Flow<Result<AccountDto>>
}
