package ru.point.impl.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.point.api.model.TransactionDto

@Serializable
data class Transaction(
    @SerialName("id") val id: Int,
    @SerialName("account") val account: AccountRef,
    @SerialName("category") val category: TransactionCategory,
    @SerialName("amount") val amount: String,
    @SerialName("transactionDate") val transactionDate: String,
    @SerialName("comment") val comment: String,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updatedAt") val updatedAt: String,
)

@Serializable
data class CreateTransactionResponse(
    @SerialName("id") val id: Int,
    @SerialName("accountId") val accountId: Int,
    @SerialName("categoryId") val categoryId: Int,
    @SerialName("amount") val amount: String,
    @SerialName("transactionDate") val transactionDate: String,
    @SerialName("comment") val comment: String? = null,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updatedAt") val updatedAt: String,
)

@Serializable
data class CreateTransactionRequest(
    @SerialName("accountId") val accountId: Int,
    @SerialName("categoryId") val categoryId: Int,
    @SerialName("amount") val amount: String,
    @SerialName("transactionDate") val transactionDate: String,
    @SerialName("comment") val comment: String?,
)

@Serializable
data class AccountRef(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("balance") val balance: String,
    @SerialName("currency") val currency: String,
)

@Serializable
data class TransactionCategory(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("emoji") val emoji: String,
    @SerialName("isIncome") val isIncome: Boolean,
)

fun Transaction.toDomain(): TransactionDto =
    TransactionDto(
        id = id,
        accountId = account.id,
        accountName = account.name,
        amount = amount,
        currency = account.currency,
        categoryId = category.id,
        categoryName = category.name,
        emoji = category.emoji,
        isIncome = category.isIncome,
        dateTime = transactionDate,
        comment = comment,
        totalAmount = account.balance,
    )
