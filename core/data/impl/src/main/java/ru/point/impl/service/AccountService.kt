package ru.point.impl.service

import retrofit2.http.GET
import ru.point.impl.model.Account

interface AccountService {
    @GET("api/v1/accounts")
    suspend fun getAccounts(): List<Account>
}
