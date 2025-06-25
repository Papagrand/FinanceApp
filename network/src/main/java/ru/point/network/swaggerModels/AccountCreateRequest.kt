package ru.point.network.swaggerModels

data class AccountCreateRequest(
    val name: String,
    val balance: String,
    val currency: String,
)
