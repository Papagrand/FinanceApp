package ru.point.categories.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.point.network.dto.MyCategoriesDto

interface CategoryService {
    @GET("api/v1/accounts/{id}")
    suspend fun getMyCategories(
        @Path("id") accountId: Int,
    ): MyCategoriesDto
}
