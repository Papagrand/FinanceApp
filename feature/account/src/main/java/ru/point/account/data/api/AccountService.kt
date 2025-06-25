package ru.point.account.data.api

import retrofit2.http.GET
import ru.point.network.dto.AccountDto

interface AccountService {
    @GET("api/v1/accounts")
    suspend fun getAccounts(): List<AccountDto>
}
