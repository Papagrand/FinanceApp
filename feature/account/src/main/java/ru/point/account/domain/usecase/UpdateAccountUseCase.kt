package ru.point.account.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.point.api.model.AccountDto
import ru.point.api.repository.AccountRepository
import ru.point.utils.common.Result
import javax.inject.Inject

internal class UpdateAccountUseCase @Inject constructor(
    private val repo: AccountRepository,
) {
    operator fun invoke(
        id: Int,
        name: String,
        balance: String,
        currency: String,
    ): Flow<Result<AccountDto>> = repo.updateAccount(id, name, balance, currency)
}
