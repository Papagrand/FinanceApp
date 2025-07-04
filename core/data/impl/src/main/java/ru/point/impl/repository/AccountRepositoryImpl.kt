package ru.point.impl.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.api.model.AccountDto
import ru.point.api.repository.AccountRepository
import ru.point.impl.flow.safeApiFlow
import ru.point.impl.model.AccountUpdateRequest
import ru.point.impl.service.AccountService
import ru.point.utils.common.Result
import ru.point.utils.common.Result.Error
import ru.point.utils.common.Result.Loading
import ru.point.utils.common.Result.Success
import javax.inject.Inject
import kotlin.collections.first

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
    override fun observe(): Flow<Result<AccountDto>> =
        safeApiFlow { api.getAccounts() }
            .map { result ->
                when (result) {
                    is Loading -> Loading
                    is Error -> Error(result.cause)
                    is Success -> {
                        val dto = result.data.first()
                        Success(
                            AccountDto(
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

    override fun updateAccount(
        id: Int,
        name: String,
        balance: String,
        currency: String,
    ): Flow<Result<AccountDto>> =
        safeApiFlow {
            api.updateAccount(
                id,
                AccountUpdateRequest(name, balance, currency),
            )
        }
            .map { result ->
                when (result) {
                    is Loading -> Loading
                    is Error -> Error(result.cause)
                    is Success -> {
                        val dto = result.data
                        Success(
                            AccountDto(
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
