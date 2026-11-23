package ru.yeahub.datastore_api

import kotlinx.coroutines.flow.Flow

/**
 * Контракт локального хранения токена авторизации:
 * - saveAccessToken - сохраняет access token
 * - getAccessToken - возвращает сохранённый access token
 * - clearTokens - очищает токены авторизации
 */
interface TokenDataStore {

    suspend fun saveAccessToken(accessToken: String): TokenStorageResult

    suspend fun getAccessToken(): String?

    fun observeAccessToken(): Flow<String?>

    suspend fun clearTokensIfMatches(
        expectedToken: String?,
    ): Boolean

    suspend fun clearTokens(): TokenStorageResult
}