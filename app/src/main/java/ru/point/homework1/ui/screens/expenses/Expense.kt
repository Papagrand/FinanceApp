package ru.point.homework1.ui.screens.expenses

data class Expense (
    val id: String,
    val title: String,
    val subtitle: String?,
    val emojiIcon: String?,
    val amount: String,
    val currency: String
)