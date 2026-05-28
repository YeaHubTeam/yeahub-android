package ru.yeahub.datastore_api

/**
 * Контракт локального хранения токена авторизации:
 * - saveAccessToken - сохраняет access token
 * - getAccessToken - возвращает сохранённый access token
 * - clearTokens - очищает токены авторизации
 */
interface TokenDataStore {

    suspend fun saveAccessToken(accessToken: String)

    suspend fun getAccessToken(): String?

    suspend fun clearTokens()
}