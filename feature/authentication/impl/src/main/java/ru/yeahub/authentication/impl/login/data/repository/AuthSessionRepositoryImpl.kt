package ru.yeahub.authentication.impl.login.data.repository

import ru.yeahub.authentication.impl.login.domain.repository.AuthSessionRepository
import ru.yeahub.datastore_api.TokenDataStore
import ru.yeahub.datastore_api.TokenStorageResult

/**
 * Реализация репозитория auth-сессии:
 * - делегирует сохранение access token в TokenDataStore
 * - делегирует чтение access token в TokenDataStore
 * - делегирует очистку токенов в TokenDataStore
 */
class AuthSessionRepositoryImpl(
    private val tokenDataStore: TokenDataStore,
) : AuthSessionRepository {

    override suspend fun saveAccessToken(accessToken: String): TokenStorageResult {
        return tokenDataStore.saveAccessToken(accessToken)
    }

    override suspend fun getAccessToken(): String? {
        return tokenDataStore.getAccessToken()
    }

    override suspend fun clearTokens(): TokenStorageResult {
        return tokenDataStore.clearTokens()
    }
}