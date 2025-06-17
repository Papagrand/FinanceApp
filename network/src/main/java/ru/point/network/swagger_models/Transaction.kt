package ru.point.network.swagger_models

data class Transaction(
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)
