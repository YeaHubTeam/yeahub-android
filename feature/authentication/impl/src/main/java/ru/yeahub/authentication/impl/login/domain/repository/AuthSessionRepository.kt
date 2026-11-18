package ru.yeahub.authentication.impl.login.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.yeahub.datastore_api.TokenStorageResult

/**
 * Контракт репозитория auth-сессии:
 * - saveAccessToken - сохраняет access token пользователя
 * - getAccessToken - возвращает сохранённый access token
 * - clearTokens - очищает сохранённые токены авторизации
 * (logout() заменяет старый clearTokens(), потому что теперь выход состоит не только из очистки токена.)
 */
interface AuthSessionRepository {

    suspend fun saveAccessToken(accessToken: String): TokenStorageResult

    suspend fun getAccessToken(): String?

    fun observeAuthState(): Flow<Boolean>

    suspend fun logout(): TokenStorageResult
}