package ru.point.expenses.domain

data class Expense (
    val id: String,
    val title: String,
    val subtitle: String?,
    val emojiIcon: String?,
    val amount: String,
    val currency: String
)