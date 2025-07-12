package ru.point.impl.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyCategories(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("balance") val balance: String,
    @SerialName("currency") val currency: String,
    @SerialName("incomeStats") val incomeStats: List<CategoryStat>,
    @SerialName("expenseStats") val expenseStats: List<CategoryStat>,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updatedAt") val updatedAt: String,
)

@Serializable
data class CategoryStat(
    @SerialName("categoryId") val categoryId: Int,
    @SerialName("categoryName") val categoryName: String,
    @SerialName("emoji") val emoji: String,
    @SerialName("amount") val amount: String,
)

@Serializable
data class AllCategories(
    @SerialName("id") val categoryId: Int,
    @SerialName("name") val categoryName: String,
    @SerialName("emoji") val emoji: String,
    @SerialName("isIncome") val isIncome: Boolean,
)
