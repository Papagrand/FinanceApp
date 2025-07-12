package ru.point.api.flow

import kotlinx.coroutines.flow.Flow

interface AccountPreferencesRepo {
    val accountIdFlow: Flow<Int?>

    val currencyFlow: Flow<String?>

    suspend fun saveAccountId(id: Int)

    suspend fun saveCurrency(code: String)
}
