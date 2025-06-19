package ru.point.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.point.network.dto.TransactionDto

interface TransactionService {

    @GET("api/v1/transactions/account/{accountId}/period")
    suspend fun getByAccountForPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") start: String,
        @Query("endDate") end: String
    ): List<TransactionDto>
}