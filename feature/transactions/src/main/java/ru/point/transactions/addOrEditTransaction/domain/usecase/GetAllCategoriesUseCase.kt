package ru.point.transactions.addOrEditTransaction.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.point.api.model.AllCategoriesDto
import ru.point.api.repository.CategoryRepository
import ru.point.utils.common.Result
import javax.inject.Inject

internal class GetAllCategoriesUseCase @Inject constructor(
    private val repo: CategoryRepository,
) {
    operator fun invoke(isIncome: Boolean): Flow<Result<List<AllCategoriesDto>>> = repo.getAllCategoriesByType(isIncome)
}
