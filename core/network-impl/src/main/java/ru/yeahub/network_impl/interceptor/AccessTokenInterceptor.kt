package ru.yeahub.network_impl.interceptor

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.yeahub.datastore_api.TokenDataStore

class AccessTokenInterceptor(
    private val tokenDataStore: TokenDataStore,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestPath = originalRequest.url().encodedPath()

        val requestToProceed = if (requestPath in PUBLIC_AUTH_PATHS) {
            originalRequest
        } else {
            val accessToken = runBlocking {
                runCatching {
                    tokenDataStore.getAccessToken()
                }.getOrNull()
            }

            if (accessToken.isNullOrBlank()) {
                originalRequest
            } else {
                originalRequest.newBuilder()
                    .header(
                        AUTHORIZATION_HEADER,
                        "$BEARER_PREFIX $accessToken",
                    )
                    .build()
            }
        }

        return chain.proceed(requestToProceed)
    }

    private companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer"

        private val PUBLIC_AUTH_PATHS = setOf(
            "/auth/login",
            "/auth/signUp",
            "/auth/refresh",
        )
    }
}