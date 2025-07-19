package ru.point.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_categories")
data class MyCategoryEntity(
    @PrimaryKey val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: String
)