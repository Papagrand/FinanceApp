package ru.point.core.common

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("account_prefs")

class AccountPreferences(private val context: Context) {
    companion object {
        private val ACCOUNT_ID_KEY = intPreferencesKey("account_id")
    }

    val accountIdFlow: Flow<Int?> = context.dataStore.data
        .map { prefs -> prefs[ACCOUNT_ID_KEY] }

    suspend fun saveAccountId(id: Int) {
        context.dataStore.edit { prefs ->
            prefs[ACCOUNT_ID_KEY] = id
        }
    }
}