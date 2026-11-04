package ru.yeahub.authentication.impl.login.data.repository

import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import ru.yeahub.authentication.impl.login.domain.repository.AuthSessionRepository
import ru.yeahub.datastore_api.TokenDataStore
import ru.yeahub.datastore_api.TokenStorageResult
import ru.yeahub.network_api.AuthCookieManager
import ru.yeahub.network_api.NetworkProvider
import timber.log.Timber
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Реализация репозитория auth-сессии:
 * - делегирует сохранение access token в TokenDataStore
 * - делегирует чтение access token в TokenDataStore
 * - делегирует очистку токенов в TokenDataStore
 */
class AuthSessionRepositoryImpl(
    private val tokenDataStore: TokenDataStore,
    private val networkProvider: NetworkProvider,
    private val authCookieManager: AuthCookieManager,
) : AuthSessionRepository {

    override suspend fun saveAccessToken(
        accessToken: String,
    ): TokenStorageResult {
        return tokenDataStore.saveAccessToken(accessToken)
    }

    override suspend fun getAccessToken(): String? {
        return tokenDataStore.getAccessToken()
    }

    override fun observeAuthState(): Flow<Boolean> {
        return tokenDataStore.observeAccessToken()
            .map { accessToken ->
                accessToken.isNullOrBlank().not()
            }
            .distinctUntilChanged()
    }

    override suspend fun logout(): TokenStorageResult {
        try {
            networkProvider.apiService.logout()
        } catch (exception: CancellationException) {
            clearLocalSession()
            throw exception
        } catch (exception: IOException) {
            Timber.w(exception, "Server logout failed")
        } catch (exception: HttpException) {
            Timber.w(exception, "Server logout failed")
        }

        return clearLocalSession()
    }

    private suspend fun clearLocalSession(): TokenStorageResult {
        authCookieManager.clear()

        return tokenDataStore.clearTokens()
    }
}