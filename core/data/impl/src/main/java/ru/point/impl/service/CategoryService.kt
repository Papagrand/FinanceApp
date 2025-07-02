package ru.point.impl.service

import retrofit2.http.GET
import retrofit2.http.Path
import ru.point.impl.model.MyCategories

interface CategoryService {
    @GET("api/v1/accounts/{id}")
    suspend fun getMyCategories(
        @Path("id") accountId: Int,
    ): MyCategories
}
