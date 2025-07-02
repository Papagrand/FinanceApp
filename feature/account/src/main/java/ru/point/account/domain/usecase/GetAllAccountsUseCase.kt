package ru.point.account.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.point.api.model.AccountDto
import ru.point.api.repository.AccountRepository
import ru.point.utils.common.Result

class GetAllAccountsUseCase(
    private val repo: AccountRepository,
) {
    operator fun invoke(): Flow<Result<AccountDto>> = repo.observe()
}
