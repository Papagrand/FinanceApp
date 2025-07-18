package ru.point.impl.service

import retrofit2.http.GET
import retrofit2.http.Path
import ru.point.impl.model.AllCategories
import ru.point.impl.model.MyCategories

interface CategoryService {
    @GET("accounts/{id}")
    suspend fun getMyCategories(
        @Path("id") accountId: Int,
    ): MyCategories

    @GET("categories")
    suspend fun getAllCategories(): List<AllCategories>
}
