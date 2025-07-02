package ru.point.impl.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.point.api.model.AccountDto

@Serializable
data class Account(
    @SerialName("id") val id: Int,
    @SerialName("userId") val userId: Int,
    @SerialName("name") val name: String,
    @SerialName("balance") val balance: String,
    @SerialName("currency") val currency: String,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updatedAt") val updatedAt: String,
)

internal val Account.asAccountDto
    get() =
        AccountDto(
            id = id,
            userId = userId,
            name = name,
            balance = balance,
            currency = currency,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
