package ru.point.impl.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.api.model.AllCategoriesDto
import ru.point.api.model.CategoryDto
import ru.point.api.repository.CategoryRepository
import ru.point.impl.flow.safeApiFlow
import ru.point.impl.service.CategoryService
import ru.point.utils.common.Result
import ru.point.utils.common.Result.Error
import ru.point.utils.common.Result.Loading
import ru.point.utils.common.Result.Success
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
    override fun observe(accountId: Int): Flow<Result<List<CategoryDto>>> =
        safeApiFlow { api.getMyCategories(accountId) }
            .map { result ->
                when (result) {
                    is Loading -> Loading
                    is Error -> Error(result.cause)
                    is Success ->
                        Success(
                            (result.data.incomeStats + result.data.expenseStats)
                                .map { dto ->
                                    CategoryDto(
                                        categoryId = dto.categoryId,
                                        categoryName = dto.categoryName,
                                        emoji = dto.emoji,
                                        amount = dto.amount,
                                    )
                                },
                        )
                }
            }

    override fun getAllCategories(isIncome: Boolean): Flow<Result<List<AllCategoriesDto>>> =
        safeApiFlow {
            api.getCategoriesByType(isIncome)
        }
            .map { result ->
                when (result) {
                    is Loading -> Loading
                    is Error -> Error(result.cause)
                    is Success ->
                        Success(
                            result.data
                                .map { dto ->
                                    AllCategoriesDto(
                                        categoryId = dto.categoryId,
                                        categoryName = dto.categoryName,
                                        emoji = dto.emoji,
                                        isIncome = dto.isIncome,
                                    )
                                },
                        )
                }
            }
}
