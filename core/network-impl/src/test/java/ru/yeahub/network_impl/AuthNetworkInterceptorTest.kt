package ru.yeahub.network_impl

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import retrofit2.Call
import retrofit2.HttpException
import ru.yeahub.datastore_api.AuthCookieDataStore
import ru.yeahub.datastore_api.TokenDataStore
import ru.yeahub.datastore_api.TokenStorageResult
import ru.yeahub.network_api.models.LoginResponseDto
import ru.yeahub.network_impl.cookie.AuthCookieJar
import ru.yeahub.network_impl.interceptor.AccessTokenInterceptor
import ru.yeahub.network_impl.interceptor.TokenRefreshInterceptor
import ru.yeahub.network_impl.refresh.RefreshTokenService
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import retrofit2.Response as RetrofitResponse

class AuthNetworkInterceptorTest {

    @Test
    fun `should add access token to protected request`() {
        val tokenDataStore = FakeTokenDataStore(OLD_ACCESS_TOKEN)
        val interceptor = AccessTokenInterceptor(tokenDataStore)
        val request = createRequest(PROTECTED_PATH)
        val proceededRequests = mutableListOf<Request>()
        val chain = createSuccessfulChain(
            request = request,
            proceededRequests = proceededRequests,
        )

        interceptor.intercept(chain)

        Assertions.assertEquals(
            "$BEARER_PREFIX $OLD_ACCESS_TOKEN",
            proceededRequests.single().header(AUTHORIZATION_HEADER),
        )
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "/auth/login",
            "/auth/signUp",
            "/auth/refresh",
        ],
    )
    fun `should not add access token to public request`(
        requestPath: String,
    ) {
        val tokenDataStore = FakeTokenDataStore(OLD_ACCESS_TOKEN)
        val interceptor = AccessTokenInterceptor(tokenDataStore)
        val request = createRequest(requestPath)
        val proceededRequests = mutableListOf<Request>()
        val chain = createSuccessfulChain(
            request = request,
            proceededRequests = proceededRequests,
        )

        interceptor.intercept(chain)

        Assertions.assertNull(
            proceededRequests.single().header(AUTHORIZATION_HEADER),
        )
    }

    @Test
    fun `should refresh token and retry unauthorized request`() {
        val tokenDataStore = FakeTokenDataStore(OLD_ACCESS_TOKEN)
        val interceptor = TokenRefreshInterceptor(
            refreshTokenService = createSuccessfulRefreshService(),
            tokenDataStore = tokenDataStore,
            authCookieJar = createAuthCookieJar(),
        )
        val request = createRequest(
            path = PROTECTED_PATH,
            accessToken = OLD_ACCESS_TOKEN,
        )
        val proceededRequests = mutableListOf<Request>()
        val chain = createUnauthorizedThenSuccessfulChain(
            request = request,
            proceededRequests = proceededRequests,
        )

        val result = interceptor.intercept(chain)

        Assertions.assertEquals(HTTP_OK, result.code())
        Assertions.assertEquals(
            NEW_ACCESS_TOKEN,
            tokenDataStore.currentToken,
        )
        Assertions.assertEquals(
            "$BEARER_PREFIX $NEW_ACCESS_TOKEN",
            proceededRequests.last().header(AUTHORIZATION_HEADER),
        )
        Assertions.assertEquals(
            RETRY_HEADER_VALUE,
            proceededRequests.last().header(RETRY_HEADER),
        )
    }

    @Test
    fun `should clear session when refresh returns unauthorized`() {
        val tokenDataStore = FakeTokenDataStore(OLD_ACCESS_TOKEN)
        val authCookieJar = mockk<AuthCookieJar>(relaxed = true)
        val interceptor = TokenRefreshInterceptor(
            refreshTokenService = createErrorRefreshService(
                code = HTTP_UNAUTHORIZED,
            ),
            tokenDataStore = tokenDataStore,
            authCookieJar = authCookieJar,
        )
        val request = createRequest(
            path = PROTECTED_PATH,
            accessToken = OLD_ACCESS_TOKEN,
        )
        val chain = createUnauthorizedChain(request)

        val result = interceptor.intercept(chain)

        Assertions.assertEquals(
            HTTP_UNAUTHORIZED,
            result.code(),
        )
        Assertions.assertNull(tokenDataStore.currentToken)
        verify(exactly = 1) {
            authCookieJar.clear()
        }
    }

    @Test
    fun `should keep session when refresh returns server error`() {
        val tokenDataStore = FakeTokenDataStore(OLD_ACCESS_TOKEN)
        val authCookieJar = mockk<AuthCookieJar>(relaxed = true)
        val interceptor = TokenRefreshInterceptor(
            refreshTokenService = createErrorRefreshService(
                code = HTTP_SERVER_ERROR,
            ),
            tokenDataStore = tokenDataStore,
            authCookieJar = authCookieJar,
        )
        val request = createRequest(
            path = PROTECTED_PATH,
            accessToken = OLD_ACCESS_TOKEN,
        )
        val chain = createUnauthorizedChain(request)

        Assertions.assertThrows(
            HttpException::class.java,
        ) {
            interceptor.intercept(chain)
        }

        Assertions.assertEquals(
            OLD_ACCESS_TOKEN,
            tokenDataStore.currentToken,
        )
        verify(exactly = 0) {
            authCookieJar.clear()
        }
    }

    @Test
    fun `should keep session when refresh fails with network error`() {
        val tokenDataStore = FakeTokenDataStore(OLD_ACCESS_TOKEN)
        val authCookieJar = mockk<AuthCookieJar>(relaxed = true)
        val refreshTokenService = mockk<RefreshTokenService>()
        val refreshCall = mockk<Call<LoginResponseDto>>()

        every {
            refreshTokenService.refreshToken()
        } returns refreshCall

        every {
            refreshCall.execute()
        } throws IOException()

        val interceptor = TokenRefreshInterceptor(
            refreshTokenService = refreshTokenService,
            tokenDataStore = tokenDataStore,
            authCookieJar = authCookieJar,
        )
        val request = createRequest(
            path = PROTECTED_PATH,
            accessToken = OLD_ACCESS_TOKEN,
        )
        val chain = createUnauthorizedChain(request)

        Assertions.assertThrows(
            IOException::class.java,
        ) {
            interceptor.intercept(chain)
        }

        Assertions.assertEquals(
            OLD_ACCESS_TOKEN,
            tokenDataStore.currentToken,
        )
        verify(exactly = 0) {
            authCookieJar.clear()
        }
    }

    @Test
    fun `should not clear new session after old refresh fails`() {
        val tokenDataStore = FakeTokenDataStore(OLD_ACCESS_TOKEN)
        val authCookieJar = mockk<AuthCookieJar>(relaxed = true)
        val refreshTokenService = mockk<RefreshTokenService>()
        val refreshCall = mockk<Call<LoginResponseDto>>()

        every {
            refreshTokenService.refreshToken()
        } returns refreshCall

        every {
            refreshCall.execute()
        } answers {
            runBlocking {
                tokenDataStore.saveAccessToken(NEW_ACCESS_TOKEN)
            }

            createErrorRetrofitResponse(
                code = HTTP_UNAUTHORIZED,
            )
        }

        val interceptor = TokenRefreshInterceptor(
            refreshTokenService = refreshTokenService,
            tokenDataStore = tokenDataStore,
            authCookieJar = authCookieJar,
        )
        val request = createRequest(
            path = PROTECTED_PATH,
            accessToken = OLD_ACCESS_TOKEN,
        )
        val chain = createUnauthorizedChain(request)

        val result = interceptor.intercept(chain)

        Assertions.assertEquals(
            HTTP_UNAUTHORIZED,
            result.code(),
        )
        Assertions.assertEquals(
            NEW_ACCESS_TOKEN,
            tokenDataStore.currentToken,
        )
        verify(exactly = 0) {
            authCookieJar.clear()
        }
    }

    @Test
    fun `should perform one refresh for concurrent unauthorized responses`() {
        val tokenDataStore = FakeTokenDataStore(OLD_ACCESS_TOKEN)
        val refreshTokenService = mockk<RefreshTokenService>()
        val refreshCall = mockk<Call<LoginResponseDto>>()
        val refreshResponse = mockk<LoginResponseDto>()
        val refreshStarted = CountDownLatch(1)
        val allowRefreshToFinish = CountDownLatch(1)
        val secondUnauthorizedReceived = CountDownLatch(1)

        every {
            refreshResponse.accessToken
        } returns NEW_ACCESS_TOKEN

        every {
            refreshTokenService.refreshToken()
        } returns refreshCall

        every {
            refreshCall.execute()
        } answers {
            refreshStarted.countDown()
            allowRefreshToFinish.await(
                LATCH_TIMEOUT_SECONDS,
                TimeUnit.SECONDS,
            )
            RetrofitResponse.success(refreshResponse)
        }

        val interceptor = TokenRefreshInterceptor(
            refreshTokenService = refreshTokenService,
            tokenDataStore = tokenDataStore,
            authCookieJar = createAuthCookieJar(),
        )
        val request = createRequest(
            path = PROTECTED_PATH,
            accessToken = OLD_ACCESS_TOKEN,
        )
        val firstChain = createUnauthorizedThenSuccessfulChain(
            request = request,
        )
        val secondChain = createUnauthorizedThenSuccessfulChain(
            request = request,
            onUnauthorized = {
                secondUnauthorizedReceived.countDown()
            },
        )

        runBlocking {
            val firstResult = async(Dispatchers.IO) {
                interceptor.intercept(firstChain)
            }

            refreshStarted.await(
                LATCH_TIMEOUT_SECONDS,
                TimeUnit.SECONDS,
            )

            val secondResult = async(Dispatchers.IO) {
                interceptor.intercept(secondChain)
            }

            secondUnauthorizedReceived.await(
                LATCH_TIMEOUT_SECONDS,
                TimeUnit.SECONDS,
            )
            allowRefreshToFinish.countDown()

            Assertions.assertEquals(
                HTTP_OK,
                firstResult.await().code(),
            )
            Assertions.assertEquals(
                HTTP_OK,
                secondResult.await().code(),
            )
        }

        verify(exactly = 1) {
            refreshCall.execute()
        }
        Assertions.assertEquals(1, tokenDataStore.saveCount)
    }

    @Test
    fun `should restore persisted cookie after cookie jar recreation`() {
        val cookieDataStore = FakeAuthCookieDataStore()
        val firstCookieJar = AuthCookieJar(cookieDataStore)
        val requestUrl = createRequest(REFRESH_PATH).url()
        val refreshCookie = Cookie.Builder()
            .name(REFRESH_COOKIE_NAME)
            .value(REFRESH_COOKIE_VALUE)
            .domain(API_DOMAIN)
            .path("/")
            .expiresAt(
                System.currentTimeMillis() + COOKIE_LIFETIME_MILLIS,
            )
            .secure()
            .httpOnly()
            .build()

        firstCookieJar.saveFromResponse(
            requestUrl,
            listOf(refreshCookie),
        )

        val recreatedCookieJar = AuthCookieJar(cookieDataStore)
        val restoredCookies = recreatedCookieJar.loadForRequest(
            requestUrl,
        )

        Assertions.assertEquals(1, restoredCookies.size)
        Assertions.assertEquals(
            REFRESH_COOKIE_NAME,
            restoredCookies.single().name(),
        )
        Assertions.assertEquals(
            REFRESH_COOKIE_VALUE,
            restoredCookies.single().value(),
        )
    }

    private fun createAuthCookieJar(): AuthCookieJar {
        return AuthCookieJar(
            authCookieDataStore = FakeAuthCookieDataStore(),
        )
    }

    private fun createSuccessfulRefreshService(): RefreshTokenService {
        val refreshTokenService = mockk<RefreshTokenService>()
        val refreshCall = mockk<Call<LoginResponseDto>>()
        val refreshResponse = mockk<LoginResponseDto>()

        every {
            refreshResponse.accessToken
        } returns NEW_ACCESS_TOKEN

        every {
            refreshTokenService.refreshToken()
        } returns refreshCall

        every {
            refreshCall.execute()
        } returns RetrofitResponse.success(refreshResponse)

        return refreshTokenService
    }

    private fun createErrorRefreshService(
        code: Int,
    ): RefreshTokenService {
        val refreshTokenService = mockk<RefreshTokenService>()
        val refreshCall = mockk<Call<LoginResponseDto>>()

        every {
            refreshTokenService.refreshToken()
        } returns refreshCall

        every {
            refreshCall.execute()
        } returns createErrorRetrofitResponse(code)

        return refreshTokenService
    }

    private fun createErrorRetrofitResponse(
        code: Int,
    ): RetrofitResponse<LoginResponseDto> {
        val mediaType = MediaType.parse(APPLICATION_JSON)
        val responseBody = ResponseBody.create(
            mediaType,
            EMPTY_JSON,
        )

        return RetrofitResponse.error(
            code,
            responseBody,
        )
    }

    private fun createSuccessfulChain(
        request: Request,
        proceededRequests: MutableList<Request>,
    ): Interceptor.Chain {
        val chain = mockk<Interceptor.Chain>()

        every {
            chain.request()
        } returns request

        every {
            chain.proceed(any())
        } answers {
            val proceededRequest = firstArg<Request>()
            proceededRequests += proceededRequest

            createResponse(
                request = proceededRequest,
                code = HTTP_OK,
            )
        }

        return chain
    }

    private fun createUnauthorizedChain(
        request: Request,
    ): Interceptor.Chain {
        val chain = mockk<Interceptor.Chain>()

        every {
            chain.request()
        } returns request

        every {
            chain.proceed(any())
        } answers {
            createResponse(
                request = firstArg(),
                code = HTTP_UNAUTHORIZED,
            )
        }

        return chain
    }

    private fun createUnauthorizedThenSuccessfulChain(
        request: Request,
        proceededRequests: MutableList<Request> = mutableListOf(),
        onUnauthorized: () -> Unit = {},
    ): Interceptor.Chain {
        val chain = mockk<Interceptor.Chain>()
        var requestCount = 0

        every {
            chain.request()
        } returns request

        every {
            chain.proceed(any())
        } answers {
            val proceededRequest = firstArg<Request>()
            proceededRequests += proceededRequest
            requestCount += 1

            if (requestCount == 1) {
                onUnauthorized()

                createResponse(
                    request = proceededRequest,
                    code = HTTP_UNAUTHORIZED,
                )
            } else {
                createResponse(
                    request = proceededRequest,
                    code = HTTP_OK,
                )
            }
        }

        return chain
    }

    private fun createRequest(
        path: String,
        accessToken: String? = null,
    ): Request {
        val requestBuilder = Request.Builder()
            .url("$BASE_URL$path")

        accessToken?.let { token ->
            requestBuilder.header(
                AUTHORIZATION_HEADER,
                "$BEARER_PREFIX $token",
            )
        }

        return requestBuilder.build()
    }

    private fun createResponse(
        request: Request,
        code: Int,
    ): Response {
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message(TEST_RESPONSE_MESSAGE)
            .build()
    }

    private class FakeTokenDataStore(
        initialToken: String?,
    ) : TokenDataStore {

        private val tokenFlow = MutableStateFlow(initialToken)

        val currentToken: String?
            get() = tokenFlow.value

        var saveCount: Int = 0
            private set

        override suspend fun saveAccessToken(
            accessToken: String,
        ): TokenStorageResult {
            tokenFlow.value = accessToken
            saveCount += 1

            return TokenStorageResult.Success
        }

        override suspend fun getAccessToken(): String? {
            return tokenFlow.value
        }

        override fun observeAccessToken(): Flow<String?> {
            return tokenFlow
        }

        override suspend fun clearTokensIfMatches(
            expectedToken: String?,
        ): Boolean {
            if (
                expectedToken.isNullOrBlank() ||
                tokenFlow.value != expectedToken
            ) {
                return false
            }

            tokenFlow.value = null
            return true
        }

        override suspend fun clearTokens(): TokenStorageResult {
            tokenFlow.value = null
            return TokenStorageResult.Success
        }
    }

    private class FakeAuthCookieDataStore(
        initialCookies: String? = null,
    ) : AuthCookieDataStore {

        private var storedCookies: String? = initialCookies

        override suspend fun saveCookies(
            cookies: String,
        ): TokenStorageResult {
            storedCookies = cookies
            return TokenStorageResult.Success
        }

        override suspend fun getCookies(): String? {
            return storedCookies
        }

        override suspend fun clearCookies(): TokenStorageResult {
            storedCookies = null
            return TokenStorageResult.Success
        }
    }

    private companion object {
        private const val BASE_URL = "https://api.yeatwork.ru"
        private const val API_DOMAIN = "api.yeatwork.ru"
        private const val PROTECTED_PATH = "/auth/profile"
        private const val REFRESH_PATH = "/auth/refresh"
        private const val OLD_ACCESS_TOKEN = "old-access-token"
        private const val NEW_ACCESS_TOKEN = "new-access-token"
        private const val REFRESH_COOKIE_NAME = "refresh_token"
        private const val REFRESH_COOKIE_VALUE = "refresh-cookie-value"
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer"
        private const val RETRY_HEADER = "X-Auth-Retry"
        private const val RETRY_HEADER_VALUE = "true"
        private const val APPLICATION_JSON = "application/json"
        private const val EMPTY_JSON = "{}"
        private const val TEST_RESPONSE_MESSAGE = "Test response"
        private const val HTTP_OK = 200
        private const val HTTP_UNAUTHORIZED = 401
        private const val HTTP_SERVER_ERROR = 500
        private const val LATCH_TIMEOUT_SECONDS = 5L
        private const val COOKIE_LIFETIME_MILLIS = 60_000L
    }
}