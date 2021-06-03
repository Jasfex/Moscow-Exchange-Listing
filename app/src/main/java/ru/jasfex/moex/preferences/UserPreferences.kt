package ru.jasfex.moex.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

data class UserPreferences(val agreementSigned: Boolean)

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val AGREEMENT_SIGNED = booleanPreferencesKey("agreement_signed")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val agreementSigned = preferences[PreferencesKeys.AGREEMENT_SIGNED] ?: false
            UserPreferences(agreementSigned = agreementSigned)
        }

    suspend fun updateAgreementSigned(agreementSigned: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.AGREEMENT_SIGNED] = agreementSigned
        }
    }

}
