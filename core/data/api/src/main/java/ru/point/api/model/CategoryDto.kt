package ru.point.api.model

data class CategoryDto(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: String,
)
