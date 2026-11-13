package ru.yeahub.network_impl.interceptor

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.yeahub.datastore_api.TokenDataStore
import ru.yeahub.datastore_api.TokenStorageResult
import ru.yeahub.network_impl.cookie.AuthCookieJar
import ru.yeahub.network_impl.refresh.RefreshTokenService
import java.io.IOException
import retrofit2.HttpException

/**
 * Обновляет access token при получении 401 и повторяет исходный запрос.
 */
class TokenRefreshInterceptor(
    private val refreshTokenService: RefreshTokenService,
    private val tokenDataStore: TokenDataStore,
    private val authCookieJar: AuthCookieJar,
) : Interceptor {

    private val refreshMutex = Mutex()
    private var failedAccessToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalResponse = chain.proceed(originalRequest)
        val requestPath = originalRequest.url().encodedPath()

        val shouldRefresh =
            originalResponse.code() == HTTP_UNAUTHORIZED &&
                    requestPath !in IGNORED_AUTH_PATHS &&
                    originalRequest.header(RETRY_HEADER) == null

        if (!shouldRefresh) {
            return originalResponse
        }

        val retryRequest = try {
            runBlocking {
                refreshMutex.withLock {
                    createRetryRequest(originalRequest)
                }
            }
        } catch (exception: IOException) {
            originalResponse.body()?.close()
            throw exception
        } catch (exception: HttpException) {
            originalResponse.body()?.close()
            throw exception
        }

        return if (retryRequest == null) {
            originalResponse
        } else {
            originalResponse.body()?.close()
            chain.proceed(retryRequest)
        }
    }

    private suspend fun createRetryRequest(
        originalRequest: Request,
    ): Request? {
        val requestToken = originalRequest
            .header(AUTHORIZATION_HEADER)
            ?.removePrefix("$BEARER_PREFIX ")

        val storedToken = tokenDataStore.getAccessToken()

        val tokenForRetry = when {
            requestToken == failedAccessToken -> null

            !storedToken.isNullOrBlank() && storedToken != requestToken -> {
                storedToken
            }

            else -> refreshAccessToken(requestToken)
        }

        return tokenForRetry?.let { accessToken ->
            originalRequest.newBuilder()
                .header(
                    AUTHORIZATION_HEADER,
                    "$BEARER_PREFIX $accessToken",
                )
                .header(RETRY_HEADER, RETRY_HEADER_VALUE)
                .build()
        }
    }

    private suspend fun refreshAccessToken(
        requestToken: String?,
    ): String? {
        val refreshResponse = refreshTokenService
            .refreshToken()
            .execute()

        if (refreshResponse.code() == HTTP_UNAUTHORIZED) {
            refreshResponse.errorBody()?.close()
            failedAccessToken = requestToken
            clearSession(requestToken)
            return null
        }

        if (!refreshResponse.isSuccessful) {
            throw HttpException(refreshResponse)
        }

        val refreshedToken = refreshResponse.body()?.accessToken

        val isTokenSaved =
            !refreshedToken.isNullOrBlank() &&
                    tokenDataStore.saveAccessToken(refreshedToken) ==
                    TokenStorageResult.Success

        if (!isTokenSaved) {
            throw IOException(
                "Refresh token response cannot be saved",
            )
        }

        failedAccessToken = null
        return refreshedToken
    }

    private suspend fun clearSession(
        requestToken: String?,
    ) {
        val tokenCleared = tokenDataStore.clearTokensIfMatches(
            expectedToken = requestToken,
        )

        if (tokenCleared) {
            authCookieJar.clear()
        }
    }

    private companion object {
        private const val HTTP_UNAUTHORIZED = 401
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer"
        private const val RETRY_HEADER = "X-Auth-Retry"
        private const val RETRY_HEADER_VALUE = "true"

        private val IGNORED_AUTH_PATHS = setOf(
            "/auth/login",
            "/auth/signUp",
            "/auth/refresh",
            "/auth/logout",
        )
    }
}
