package ru.point.account.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.point.api.model.ChartEntry
import ru.point.api.repository.ChartRepository
import ru.point.utils.common.Result

internal class GetChartsUseCase @Inject constructor(
    private val repo: ChartRepository,
) {

    operator fun invoke(accountId: Int): Flow<Result<List<ChartEntry>>> = repo.dayDiff(accountId)

}