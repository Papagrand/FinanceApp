package ru.point.account.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.point.api.model.AccountDto
import ru.point.api.repository.AccountRepository
import ru.point.utils.common.Result
import javax.inject.Inject

internal class GetAllAccountsUseCase @Inject constructor(
    private val repo: AccountRepository,
) {
    operator fun invoke(): Flow<Result<AccountDto>> = repo.observe()
}
