package ru.point.api.repository

import kotlinx.coroutines.flow.Flow
import ru.point.api.model.AllCategoriesDto
import ru.point.api.model.CategoryDto
import ru.point.utils.common.Result

interface CategoryRepository {
    fun observe(accountId: Int): Flow<Result<List<CategoryDto>>>

    fun getAllCategories(isIncome: Boolean): Flow<Result<List<AllCategoriesDto>>>
}
