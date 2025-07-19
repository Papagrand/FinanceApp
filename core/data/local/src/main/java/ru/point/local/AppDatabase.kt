package ru.point.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.point.local.dao.AccountDao
import ru.point.local.dao.CategoryDao
import ru.point.local.dao.TransactionDao
import ru.point.local.entities.AccountEntity
import ru.point.local.entities.AllCategoryEntity
import ru.point.local.entities.MyCategoryEntity
import ru.point.local.entities.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        AllCategoryEntity::class,
        MyCategoryEntity::class,
        AccountEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
}