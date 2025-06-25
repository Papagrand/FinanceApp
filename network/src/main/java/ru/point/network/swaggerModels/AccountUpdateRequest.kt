package ru.point.network.swaggerModels

data class AccountUpdateRequest(
    val name: String,
    val balance: String,
    val currency: String,
)
