package ru.point.homework1.swagger_models

data class AccountCreateRequest(
    val name: String,
    val balance: String,
    val currency: String
)
