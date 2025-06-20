package ru.point.categories.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.point.categories.domain.model.Category
import ru.point.categories.domain.repository.CategoryRepository
import ru.point.core.common.Result

class ObserveCategoriesUseCase(
    private val repo: CategoryRepository,
) {
    operator fun invoke(accountId: Int): Flow<Result<List<Category>>> = repo.observe(accountId)
}