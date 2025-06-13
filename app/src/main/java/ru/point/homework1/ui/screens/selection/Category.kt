package ru.point.homework1.ui.screens.selection

data class Category(
    val id: Int,
    val name: String,
    val emoji: String? = null,
    val isIncome: Boolean
)