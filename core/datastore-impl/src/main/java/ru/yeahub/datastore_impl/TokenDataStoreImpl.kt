package ru.yeahub.datastore_impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.yeahub.datastore_api.TokenDataStore
import ru.yeahub.datastore_api.TokenStorageResult
import ru.yeahub.datastore_impl.crypto.CryptoManager
import java.io.IOException

/**
 * Реализация локального хранения токена через Preferences DataStore:
 * - шифрует access token перед сохранением
 * - читает и расшифровывает access token
 * - очищает токены авторизации
 */
class TokenDataStoreImpl(
    private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager,
) : TokenDataStore {

    override suspend fun saveAccessToken(
        accessToken: String,
    ): TokenStorageResult {
        return runCatching {
            dataStore.edit { preferences ->
                val encryptedToken = cryptoManager.encrypt(accessToken)
                preferences[ACCESS_TOKEN_KEY] = encryptedToken
            }
        }.fold(
            onSuccess = {
                TokenStorageResult.Success
            },
            onFailure = { exception ->
                if (exception is CancellationException) {
                    throw exception
                }

                TokenStorageResult.Error
            },
        )
    }

    override suspend fun getAccessToken(): String? {
        val encryptedToken =
            dataStore.data.first()[ACCESS_TOKEN_KEY]
                ?: return null

        return runCatching {
            cryptoManager.decrypt(encryptedToken)
        }.getOrNull()
    }

    override fun observeAccessToken(): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[ACCESS_TOKEN_KEY]?.let { encryptedToken ->
                    runCatching {
                        cryptoManager.decrypt(encryptedToken)
                    }.getOrNull()
                }
            }
    }

    override suspend fun clearTokensIfMatches(
        expectedToken: String?,
    ): Boolean {
        if (expectedToken.isNullOrBlank()) {
            return false
        }

        return runCatching {
            var tokenCleared = false

            dataStore.edit { preferences ->
                val currentToken = preferences[ACCESS_TOKEN_KEY]
                    ?.let { encryptedToken ->
                        runCatching {
                            cryptoManager.decrypt(encryptedToken)
                        }.getOrNull()
                    }

                if (currentToken == expectedToken) {
                    preferences.remove(ACCESS_TOKEN_KEY)
                    tokenCleared = true
                }
            }

            tokenCleared
        }.fold(
            onSuccess = { tokenCleared ->
                tokenCleared
            },
            onFailure = { exception ->
                if (exception is CancellationException) {
                    throw exception
                }

                false
            },
        )
    }

    override suspend fun clearTokens(): TokenStorageResult {
        return runCatching {
            dataStore.edit { preferences ->
                preferences.remove(ACCESS_TOKEN_KEY)
            }
        }.fold(
            onSuccess = {
                TokenStorageResult.Success
            },
            onFailure = { exception ->
                if (exception is CancellationException) {
                    throw exception
                }

                TokenStorageResult.Error
            },
        )
    }

    private companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    }
}