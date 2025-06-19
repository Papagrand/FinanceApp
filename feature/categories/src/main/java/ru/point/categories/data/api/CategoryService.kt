package ru.point.categories.data.api

import ru.point.network.dto.CategoryDto
import retrofit2.http.GET

interface CategoryService {

    @GET("api/v1/categories")
    suspend fun getAll(): List<CategoryDto>
}