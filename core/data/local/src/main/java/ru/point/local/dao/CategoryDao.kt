package ru.point.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.point.local.entities.AllCategoryEntity
import ru.point.local.entities.MyCategoryEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM my_categories")
    fun observeMyCategories(): Flow<List<MyCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMy(list: List<MyCategoryEntity>)

    @Query("SELECT * FROM all_categories WHERE isIncome = :isIncome")
    fun observeAllByType(isIncome: Boolean): Flow<List<AllCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(list: List<AllCategoryEntity>)

    @Query("SELECT categoryName FROM all_categories WHERE categoryId = :categoryId")
    suspend fun getCategoryNameById(categoryId: Int): String
}
