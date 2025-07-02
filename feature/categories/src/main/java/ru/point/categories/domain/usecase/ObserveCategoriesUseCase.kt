package ru.point.categories.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.point.api.model.CategoryDto
import ru.point.api.repository.CategoryRepository
import ru.point.utils.common.Result

class ObserveCategoriesUseCase(
    private val repo: CategoryRepository,
) {
    operator fun invoke(accountId: Int): Flow<Result<List<CategoryDto>>> = repo.observe(accountId)
}
