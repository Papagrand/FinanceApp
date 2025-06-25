package ru.point.account.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.point.account.domain.model.Account
import ru.point.account.domain.repository.AccountRepository
import ru.point.core.common.Result

class GetAllAccountsUseCase (
    private val repo: AccountRepository
) {
    operator fun invoke(): Flow<Result<Account>> = repo.observe()
}