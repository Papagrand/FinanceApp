package ru.point.categories.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import ru.point.categories.data.api.CategoryService
import ru.point.categories.domain.model.Category
import ru.point.categories.domain.repository.CategoryRepository
import ru.point.network.client.RetrofitProvider
import ru.point.network.flow.safeApiFlow
import ru.point.core.common.Result.*
import ru.point.core.common.Result

class CategoryRepositoryImpl(
    retrofit: Retrofit = RetrofitProvider.instance
) : CategoryRepository {

    private val api = retrofit.create(CategoryService::class.java)

    override fun observe(accountId: Int): Flow<Result<List<Category>>> =
        safeApiFlow { api.getMyCategories(accountId) }
            .map { result ->
                when (result) {
                    is Loading -> Loading
                    is Error -> Error(result.cause)
                    is Success -> Success(
                        (result.data.incomeStats + result.data.expenseStats)
                            .map { dto ->
                                Category(
                                    categoryId = dto.categoryId,
                                    categoryName = dto.categoryName,
                                    emoji = dto.emoji,
                                    amount = dto.amount
                                )
                            }
                    )
                }
            }
}