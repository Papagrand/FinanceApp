package ru.point.api.model

data class AllCategoriesDto(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val isIncome: Boolean,
)
