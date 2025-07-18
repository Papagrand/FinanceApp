package ru.point.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.point.local.entities.AccountEntity

@Dao
interface AccountDao {

    @Query("SELECT * FROM accounts LIMIT 1")
    fun observe(): Flow<AccountEntity?>

    @Upsert
    suspend fun upsert(entity: AccountEntity)

    @Query("SELECT * FROM accounts WHERE isSynced = 0")
    suspend fun pending(): List<AccountEntity>

    @Query(
        """
        UPDATE accounts
           SET isSynced = 1,
               updatedAt = :updatedAt,
               updatedAtMillis = :updatedAtMillis
         WHERE id = :id
         """
    )
    suspend fun markSynced(
        id: Int,
        updatedAt: String,
        updatedAtMillis: Long
    )
}