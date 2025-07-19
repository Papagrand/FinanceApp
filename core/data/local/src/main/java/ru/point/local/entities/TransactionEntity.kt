package ru.point.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "transactions",indices = [Index(value = ["remoteId"], unique = true)])
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0L,
    val localUid: String = UUID.randomUUID().toString(),
    val remoteId: Int,
    val accountId: Int,
    val accountName: String,
    val amount: String,
    val currency: String,
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val isIncome: Boolean,
    val dateTime: String,
    val comment: String? = null,
    val totalAmount: String,
    val updatedAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false
)