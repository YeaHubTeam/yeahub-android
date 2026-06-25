package ru.yeahub.authentication.impl.login.domain.repository

import ru.yeahub.datastore_api.TokenStorageResult

/**
 * Контракт репозитория auth-сессии:
 * - saveAccessToken - сохраняет access token пользователя
 * - getAccessToken - возвращает сохранённый access token
 * - clearTokens - очищает сохранённые токены авторизации
 */
interface AuthSessionRepository {

    suspend fun saveAccessToken(accessToken: String): TokenStorageResult

    suspend fun getAccessToken(): String?

    suspend fun clearTokens(): TokenStorageResult
}