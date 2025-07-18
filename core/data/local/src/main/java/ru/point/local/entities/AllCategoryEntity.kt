package ru.point.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_categories")
data class AllCategoryEntity(
    @PrimaryKey val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val isIncome: Boolean
)