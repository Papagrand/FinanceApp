package ru.point.categories.data.api

import ru.point.network.dto.MyCategoriesDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryService {

    @GET("api/v1/accounts/{id}")
    suspend fun getMyCategories(
        @Path("id") accountId: Int
    ): MyCategoriesDto

}