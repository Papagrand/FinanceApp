package ru.point.impl.model

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import ru.point.api.model.CreateTransactionResponseDto
import ru.point.api.model.TransactionDto
import ru.point.local.entities.TransactionEntity

fun Transaction.toDto(): TransactionDto =
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

fun TransactionDto.toEntity(isSynced: Boolean): TransactionEntity =
    TransactionEntity(
        remoteId = this.id,
        accountId = this.accountId,
        accountName = this.accountName,
        amount = this.amount,
        currency = this.currency,
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        emoji = this.emoji,
        isIncome = this.isIncome,
        dateTime = this.dateTime,
        comment = this.comment,
        totalAmount = this.totalAmount,
        updatedAt = System.currentTimeMillis(),
        isSynced = isSynced,
        isDeleted = false
    )

fun CreateTransactionResponse.fromCreateTransactionToEntity(
    accountName: String,
    currency: String,
    totalAmount: String,
    categoryName: String,
    emoji: String,
    isIncome: Boolean
): TransactionEntity {
    val parsedUpdatedAt = Instant.parse(this.updatedAt)
        .toEpochMilli()

    return TransactionEntity(
        localId = 0L,
        remoteId = this.id,
        accountId = this.accountId,
        accountName = accountName,
        amount = this.amount,
        currency = currency,
        categoryId = this.categoryId,
        categoryName = categoryName,
        emoji = emoji,
        isIncome = isIncome,
        dateTime = this.transactionDate,
        comment = this.comment,
        totalAmount = totalAmount,
        updatedAt = parsedUpdatedAt,
        isSynced = true,
        isDeleted = false
    )
}

fun TransactionEntity.toCreateRequest() = CreateTransactionRequest(
    accountId = accountId,
    categoryId = categoryId,
    amount = amount,
    transactionDate = dateTime,
    comment = comment
)

fun CreateTransactionResponse.toCreateTransactionDto(): CreateTransactionResponseDto =
    CreateTransactionResponseDto(
        id = this.id,
        accountId = this.accountId,
        categoryId = this.categoryId,
        amount = this.amount,
        transactionDate = this.transactionDate,
        comment = this.comment,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )

fun TransactionEntity.entityToDto(): TransactionDto =
    TransactionDto(
        id = this.remoteId ?: this.localId.toInt(),
        accountId = this.accountId,
        accountName = this.accountName,
        amount = this.amount,
        currency = this.currency,
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        emoji = this.emoji,
        isIncome = this.isIncome,
        dateTime = this.dateTime,
        comment = this.comment,
        totalAmount = this.totalAmount
    )

fun TransactionEntity.toCreateResponseDto(): CreateTransactionResponseDto {
    val isoNow = Instant.ofEpochMilli(this.updatedAt)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ISO_INSTANT)

    return CreateTransactionResponseDto(
        id = this.localId.toInt(),
        accountId = this.accountId,
        categoryId = this.categoryId,
        amount = this.amount,
        transactionDate = this.dateTime,
        comment = this.comment,
        createdAt = isoNow,
        updatedAt = isoNow
    )
}