package ru.point.api.repository

import kotlinx.coroutines.flow.Flow
import ru.point.api.model.ChartEntry
import ru.point.utils.common.Result

interface ChartRepository {
    fun dayDiff(accountId: Int): Flow<Result<List<ChartEntry>>>
}