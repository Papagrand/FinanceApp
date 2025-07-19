package ru.point.impl.repository

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.point.api.model.AllCategoriesDto
import ru.point.api.model.CategoryDto
import ru.point.api.repository.CategoryRepository
import ru.point.impl.model.toAllCategoriesDto
import ru.point.impl.model.toAllEntity
import ru.point.impl.model.toCategoryDto
import ru.point.impl.model.toMyEntity
import ru.point.impl.service.CategoryService
import ru.point.local.dao.CategoryDao
import ru.point.utils.common.Result
import ru.point.utils.common.Result.Error
import ru.point.utils.common.Result.Loading
import ru.point.utils.common.Result.Success
import ru.point.utils.model.toAppError

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
    private val dao: CategoryDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CategoryRepository {

    override fun observe(accountId: Int): Flow<Result<List<CategoryDto>>> = channelFlow {
        send(Loading)
        dao.observeMyCategories()
            .map { list -> Success(list.map { it.toCategoryDto() }) }
            .collect { send(it) }
    }.catch { e -> emit(Error(e.toAppError())) }
        .flowOn(dispatcher)

    override suspend fun refreshAllCategories() = withContext(dispatcher) {
        val remoteList = api.getAllCategories()
        dao.upsertAll(remoteList.map { it.toAllEntity() })
    }

    override suspend fun refreshMyCategories(accountId: Int) {
        val cached = dao.observeMyCategories().first()
        if (cached.isEmpty()) {
            val remote = api.getMyCategories(accountId)
            val entities = (remote.incomeStats + remote.expenseStats)
                .map { it.toMyEntity() }
            dao.upsertMy(entities)
        }
    }

    override fun getAllCategoriesByType(isIncome: Boolean): Flow<Result<List<AllCategoriesDto>>> =
        dao.observeAllByType(isIncome)
            .map { list -> Success(list.map { it.toAllCategoriesDto() }) }
            .flowOn(dispatcher)

}
