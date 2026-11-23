package ru.yeahub.datastore_impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import ru.yeahub.datastore_api.AuthCookieDataStore
import ru.yeahub.datastore_api.TokenStorageResult
import ru.yeahub.datastore_impl.crypto.CryptoManager

class AuthCookieDataStoreImpl(
    private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager,
) : AuthCookieDataStore {

    override suspend fun saveCookies(
        cookies: String,
    ): TokenStorageResult {
        return runCatching {
            dataStore.edit { preferences ->
                val encryptedCookies = cryptoManager.encrypt(cookies)
                preferences[AUTH_COOKIES_KEY] = encryptedCookies
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

    override suspend fun getCookies(): String? {
        val encryptedCookies =
            dataStore.data.first()[AUTH_COOKIES_KEY]
                ?: return null

        return runCatching {
            cryptoManager.decrypt(encryptedCookies)
        }.getOrNull()
    }

    override suspend fun clearCookies(): TokenStorageResult {
        return runCatching {
            dataStore.edit { preferences ->
                preferences.remove(AUTH_COOKIES_KEY)
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
        private val AUTH_COOKIES_KEY =
            stringPreferencesKey("auth_cookies")
    }
}