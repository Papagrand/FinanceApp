package ru.point.impl.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.point.impl.model.Transaction

/**
 * TransactionService
 *
 * Ответственность:
 * - объявлять HTTP методы для получения списка транзакций за период.
 */

interface TransactionService {
    @GET("api/v1/transactions/account/{accountId}/period")
    suspend fun getByAccountForPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") start: String,
        @Query("endDate") end: String,
    ): List<Transaction>
}
