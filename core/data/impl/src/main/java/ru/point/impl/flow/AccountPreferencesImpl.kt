package ru.point.impl.flow

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.point.api.flow.AccountPreferencesRepo

private val Context.dataStore by preferencesDataStore("account_prefs")

class AccountPreferencesImpl(private val context: Context) : AccountPreferencesRepo {
    companion object {
        private val ACCOUNT_ID_KEY = intPreferencesKey("account_id")
        private val CURRENCY_KEY = stringPreferencesKey("currency")
    }

    override val accountIdFlow: Flow<Int?> =
        context.dataStore.data
            .map { prefs -> prefs[ACCOUNT_ID_KEY] }

    override val currencyFlow: Flow<String?> =
        context.dataStore.data.map { it[CURRENCY_KEY] }

    override suspend fun saveAccountId(id: Int) {
        context.dataStore.edit { prefs ->
            prefs[ACCOUNT_ID_KEY] = id
        }
    }

    override suspend fun saveCurrency(code: String) {
        context.dataStore.edit { it[CURRENCY_KEY] = code }
    }
}
