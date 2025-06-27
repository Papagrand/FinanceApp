package ru.point.network.swaggerModels

data class AccountResponse(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: StateItem,
    val expenseStats: StateItem,
    val createdAt: String,
    val updatedAt: String,
)
