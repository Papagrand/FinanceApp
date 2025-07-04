package ru.point.impl.service

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.point.impl.model.Account
import ru.point.impl.model.AccountUpdateRequest

interface AccountService {
    @GET("accounts")
    suspend fun getAccounts(): List<Account>

    @PUT("accounts/{id}")
    suspend fun updateAccount(
        @Path("id") accountId: Int,
        @Body request: AccountUpdateRequest,
    ): Account
}
