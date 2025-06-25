package ru.point.account.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.account.data.api.AccountService
import ru.point.account.domain.model.Account
import ru.point.account.domain.repository.AccountRepository
import ru.point.core.common.Result
import ru.point.core.common.Result.Error
import ru.point.core.common.Result.Loading
import ru.point.core.common.Result.Success
import ru.point.network.flow.safeApiFlow
import javax.inject.Inject

/**
 * AccountRepositoryImpl
 *
 * Ответственность:
 * - обращаться к REST API через Retrofit для получения данных аккаунта;
 * - преобразовывать DTO в доменную модель Account;
 * - оборачивать результаты в поток Flow<Result<Account>> через safeApiFlow.
 */

class AccountRepositoryImpl @Inject constructor(
    private val api: AccountService,
) : AccountRepository {
    override fun observe(): Flow<Result<Account>> =
        safeApiFlow { api.getAccounts() }
            .map { result ->
                when (result) {
                    is Loading -> Loading
                    is Error -> Error(result.cause)
                    is Success -> {
                        val dto = result.data.first()
                        Success(
                            Account(
                                id = dto.id,
                                userId = dto.userId,
                                name = dto.name,
                                balance = dto.balance,
                                currency = dto.currency,
                                createdAt = dto.createdAt,
                                updatedAt = dto.updatedAt,
                            ),
                        )
                    }
                }
            }
}
