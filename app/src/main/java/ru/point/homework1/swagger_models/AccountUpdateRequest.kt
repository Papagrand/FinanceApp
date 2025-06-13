package ru.point.homework1.swagger_models

data class AccountUpdateRequest(
    val name: String,
    val balance: String,
    val currency: String
)