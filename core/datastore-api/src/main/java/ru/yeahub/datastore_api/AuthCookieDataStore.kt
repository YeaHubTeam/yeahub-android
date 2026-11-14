package ru.yeahub.datastore_api

interface AuthCookieDataStore {

    suspend fun saveCookies(
        cookies: String,
    ): TokenStorageResult

    suspend fun getCookies(): String?

    suspend fun clearCookies(): TokenStorageResult
}