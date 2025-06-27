package ru.point.categories.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.point.categories.domain.model.Category
import ru.point.core.common.Result

interface CategoryRepository {
    fun observe(accountId: Int): Flow<Result<List<Category>>>
}
