package ru.point.api.model

data class TransactionDto(
    val id: Int,
    val accountId: Int,
    val accountName: String,
    val amount: String,
    val currency: String,
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val isIncome: Boolean,
    val dateTime: String,
    val comment: String? = null,
    val totalAmount: String,
)
