package ru.point.impl.service

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.point.impl.model.CreateTransactionRequest
import ru.point.impl.model.CreateTransactionResponse
import ru.point.impl.model.Transaction

/**
 * TransactionService
 *
 * Ответственность:
 * - объявлять HTTP методы для получения списка транзакций за период.
 */

interface TransactionService {
    @POST("transactions")
    suspend fun createNewTransaction(
        @Body createTransactionRequest: CreateTransactionRequest,
    ): CreateTransactionResponse

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(
        @Path("id") transactionId: Int,
    ): Unit

    @GET("transactions/account/{accountId}/period")
    suspend fun getByAccountForPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") start: String,
        @Query("endDate") end: String,
    ): List<Transaction>

    @GET("transactions/{id}")
    suspend fun getTransactionById(
        @Path("id") transactionId: Int,
    ): Transaction

    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") transactionId: Int,
        @Body createTransactionRequest: CreateTransactionRequest,
    ): Transaction
}
