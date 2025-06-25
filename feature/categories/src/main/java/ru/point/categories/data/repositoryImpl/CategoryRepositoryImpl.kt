package ru.point.categories.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.categories.data.api.CategoryService
import ru.point.categories.domain.model.Category
import ru.point.categories.domain.repository.CategoryRepository
import ru.point.core.common.Result
import ru.point.core.common.Result.Error
import ru.point.core.common.Result.Loading
import ru.point.core.common.Result.Success
import ru.point.network.flow.safeApiFlow
import javax.inject.Inject

/**
 * CategoryRepositoryImpl
 *
 * Ответственность:
 * - обращение к REST-API через Retrofit для получения категорий пользователя;
 * - преобразование полученных DTO в доменную модель Category;
 * - обёртывание результата в Flow<Result<List<Category>>> через safeApiFlow.
 */

class CategoryRepositoryImpl @Inject constructor(
    private val api: CategoryService,
) : CategoryRepository {
    override fun observe(accountId: Int): Flow<Result<List<Category>>> =
        safeApiFlow { api.getMyCategories(accountId) }
            .map { result ->
                when (result) {
                    is Loading -> Loading
                    is Error -> Error(result.cause)
                    is Success ->
                        Success(
                            (result.data.incomeStats + result.data.expenseStats)
                                .map { dto ->
                                    Category(
                                        categoryId = dto.categoryId,
                                        categoryName = dto.categoryName,
                                        emoji = dto.emoji,
                                        amount = dto.amount,
                                    )
                                },
                        )
                }
            }
}
