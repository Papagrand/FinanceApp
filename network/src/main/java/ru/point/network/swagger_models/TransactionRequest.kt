package ru.point.network.swagger_models

data class TransactionRequest(
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String?
)