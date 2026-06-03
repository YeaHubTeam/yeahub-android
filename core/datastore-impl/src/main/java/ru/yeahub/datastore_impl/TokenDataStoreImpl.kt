package ru.yeahub.datastore_impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import ru.yeahub.datastore_api.TokenDataStore
import ru.yeahub.datastore_impl.crypto.CryptoManager

/**
 * Реализация локального хранения токена через Preferences DataStore:
 * - сохраняет access token
 * - читает access token
 * - очищает токены авторизации
 */
class TokenDataStoreImpl(
    private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager,
) : TokenDataStore {

    override suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            val encryptedToken = cryptoManager.encrypt(accessToken)
            preferences[ACCESS_TOKEN_KEY] = encryptedToken
        }
    }

    override suspend fun getAccessToken(): String? {
        val encryptedToken =
            dataStore.data.first()[ACCESS_TOKEN_KEY]
                ?: return null

        return runCatching {
            cryptoManager.decrypt(encryptedToken)
        }.getOrNull()
    }

    override suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
        }
    }

    private companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    }
}