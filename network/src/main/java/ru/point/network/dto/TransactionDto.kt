package ru.point.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val id: Int,
    val account: AccountRefDto,
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class AccountRefDto(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String
)