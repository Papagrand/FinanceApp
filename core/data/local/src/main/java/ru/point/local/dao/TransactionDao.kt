package ru.point.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.point.local.entities.TransactionEntity

@Dao
interface TransactionDao {

    @Query("""
      SELECT * FROM transactions
       WHERE accountId = :acc
         AND date(dateTime) BETWEEN :from AND :to
         AND isDeleted = 0
       ORDER BY dateTime DESC
    """)
    fun observePeriod(acc: Int, from: String, to: String): Flow<List<TransactionEntity>>

    @Query("""
      SELECT COUNT(*) > 0
      FROM transactions
      WHERE accountId = :acc
        AND date(dateTime) BETWEEN :from AND :to
        AND isDeleted = 0
    """)
    suspend fun hasData(acc: Int, from: String, to: String): Boolean

    @Upsert
    suspend fun upsert(entity: TransactionEntity)

    @Upsert
    suspend fun upsert(list: List<TransactionEntity>)

    @Query("""
      UPDATE transactions 
         SET isSynced = 1,
             remoteId = :remoteId
       WHERE localUid = :localUiId
    """)
    suspend fun markSynced(localUiId: String, remoteId: Int)

    @Query("SELECT * FROM transactions WHERE isSynced = 0 AND isDeleted = 0")
    suspend fun pending(): List<TransactionEntity>

    @Query("""
      SELECT * FROM transactions
      WHERE isSynced = 0
    """)
    suspend fun pendingAll(): List<TransactionEntity>

    @Query("""
      UPDATE transactions
         SET isDeleted = 1,
             isSynced = 0
       WHERE remoteId = :remoteId
    """)
    suspend fun softDelete(remoteId: Int)

    @Query("""
      DELETE FROM transactions
       WHERE localUid = :localUid
    """)
    suspend fun hardDeleteLocal(localUid: String)

    @Query("""
      DELETE FROM transactions
       WHERE remoteId = :remoteId
    """)
    suspend fun hardDeleteWithOnline(remoteId: Int)

    @Query("SELECT * FROM transactions WHERE remoteId = :remoteId LIMIT 1")
    suspend fun getByRemoteId(remoteId: Int): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE remoteId = :remoteId LIMIT 1")
    fun observeByRemoteId(remoteId: Int): Flow<TransactionEntity?>

    @Query("""
      SELECT * FROM transactions 
       WHERE remoteId = :remoteId 
       LIMIT 1
    """)
    suspend fun requireByRemoteId(remoteId: Int): TransactionEntity

    @Query("SELECT * FROM transactions WHERE accountId = :acc AND date(dateTime) BETWEEN :from AND :to")
    suspend fun rawPeriod(acc: Int, from: String, to: String): List<TransactionEntity>

    @Query("""
      SELECT * FROM transactions
       WHERE accountId = :acc
         AND isDeleted = 0
       ORDER BY dateTime DESC
    """)
    suspend fun getAllByAccountDesc(acc: Int): List<TransactionEntity>

}