package ru.point.api.model

data class CreateTransactionResponseDto(
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null,
    val createdAt: String,
    val updatedAt: String,
)
