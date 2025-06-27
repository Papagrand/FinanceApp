package ru.point.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class MyCategoriesDto(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: List<CategoryStatDto>,
    val expenseStats: List<CategoryStatDto>,
    val createdAt: String,
    val updatedAt: String,
)

@Serializable
data class CategoryStatDto(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: String,
)

@Serializable
data class CategoryDto(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean,
)
