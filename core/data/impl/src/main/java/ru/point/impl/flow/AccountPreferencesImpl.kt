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
        private val ACCOUNT_NAME = stringPreferencesKey("account_name")
        private val CURRENCY_KEY = stringPreferencesKey("currency")
        private val LAST_UPDATE_DATE = stringPreferencesKey("last_update_date")
    }

    override val accountIdFlow: Flow<Int?> =
        context.dataStore.data
            .map { prefs -> prefs[ACCOUNT_ID_KEY] }

    override val currencyFlow: Flow<String?> =
        context.dataStore.data.map { it[CURRENCY_KEY] }

    override val lastUpdateFlow: Flow<String?> =
        context.dataStore.data.map { it[LAST_UPDATE_DATE] }


    override val accountNameFlow: Flow<String?> =
        context.dataStore.data.map { it[ACCOUNT_NAME] }

    override suspend fun saveAccountId(id: Int) {
        context.dataStore.edit { prefs ->
            prefs[ACCOUNT_ID_KEY] = id
        }
    }

    override suspend fun saveCurrency(code: String) {
        context.dataStore.edit { it[CURRENCY_KEY] = code }
    }

    override suspend fun saveAccountName(name: String){
        context.dataStore.edit { it[ACCOUNT_NAME] = name }
    }

    override suspend fun updateLastSync(date: String) {
        context.dataStore.edit { it[LAST_UPDATE_DATE] = date }
    }
}
