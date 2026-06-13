package ru.yeahub.authentication.impl.login.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import ru.yeahub.authentication.impl.login.data.mapper.LoginDomainToDataMapper
import ru.yeahub.authentication.impl.login.data.mapper.LoginResponseToDomainMapper
import ru.yeahub.authentication.impl.login.data.repository.remote.LoginRemoteDataSourceApi
import ru.yeahub.authentication.impl.login.domain.entity.AuthResult
import ru.yeahub.authentication.impl.login.domain.entity.Failure
import ru.yeahub.authentication.impl.login.domain.entity.LoginError
import ru.yeahub.authentication.impl.login.domain.entity.LoginException
import ru.yeahub.authentication.impl.login.domain.entity.LoginModel
import ru.yeahub.authentication.impl.login.domain.repository.LoginRepositoryApi
import ru.yeahub.network_api.models.ErrorResponseDto
import java.io.IOException

/**
 * Реализация репозитория авторизации:
 * - преобразует domain модель в DTO
 * - вызывает remote data source
 * - преобразует DTO в domain модель
 * - маппит сетевые и backend ошибки в LoginException
 */
class LoginRepositoryImpl(
    private val remoteDataSourceApi: LoginRemoteDataSourceApi,
    private val domainToDataMapper: LoginDomainToDataMapper,
    private val responseToDomainMapper: LoginResponseToDomainMapper,
    private val gson: Gson,
) : LoginRepositoryApi {

    /**
     * Выполняет авторизацию пользователя:
     * - преобразует LoginModel в request DTO
     * - вызывает backend
     * - преобразует response DTO в AuthResult
     */
    override suspend fun login(loginModel: LoginModel): AuthResult {
        return try {
            val request = domainToDataMapper.map(loginModel)
            val response = remoteDataSourceApi.login(request)

            responseToDomainMapper.map(response)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: IOException) {
            throw LoginException(
                error = LoginError.Network,
                failure = Failure(cause = exception),
            )
        } catch (exception: HttpException) {
            throw mapHttpException(exception)
        }
    }

    /**
     * Маппит HTTP-ошибку backend в domain ошибку авторизации:
     * - сначала пытается прочитать backend key
     * - если key неизвестен, использует HTTP status code
     */
    private fun mapHttpException(exception: HttpException): LoginException {
        val code = exception.code()
        val backendKey = exception.getBackendErrorKey()

        val error = when (backendKey) {
            BACKEND_KEY_INVALID_PASSWORD -> LoginError.InvalidPassword
            BACKEND_KEY_INVALID_CREDENTIALS -> LoginError.InvalidCredentials
            BACKEND_KEY_USER_NOT_FOUND -> LoginError.UserNotFound
            BACKEND_KEY_ACCOUNT_BLOCKED -> LoginError.AccountBlocked
            BACKEND_KEY_TOO_MANY_ATTEMPTS -> LoginError.TooManyAttempts
            BACKEND_KEY_EMAIL_NOT_CONFIRMED -> LoginError.EmailNotConfirmed
            else -> mapHttpCodeToError(code)
        }

        return LoginException(
            error = error,
            failure = Failure(
                cause = exception,
                httpCode = code,
            ),
        )
    }

    /**
     * Маппит HTTP status code в domain ошибку авторизации:
     * - используется как fallback, если backend key отсутствует или неизвестен
     */
    private fun mapHttpCodeToError(code: Int): LoginError {
        return when (code) {
            HTTP_BAD_REQUEST,
            HTTP_UNAUTHORIZED -> LoginError.InvalidCredentials

            HTTP_NOT_FOUND -> LoginError.UserNotFound

            in HTTP_SERVER_ERROR_MIN..HTTP_SERVER_ERROR_MAX -> LoginError.Server

            else -> LoginError.Unknown
        }
    }

    /**
     * Извлекает backend key из error body:
     * - парсит ErrorResponseDto
     * - возвращает null, если body пустой или формат неизвестен
     */
    private fun HttpException.getBackendErrorKey(): String? {
        val errorBody = response()?.errorBody()?.string() ?: return null

        return runCatching {
            gson.fromJson(errorBody, ErrorResponseDto::class.java).key
        }.getOrNull()
    }

    private companion object {
        private const val HTTP_BAD_REQUEST = 400
        private const val HTTP_UNAUTHORIZED = 401
        private const val HTTP_NOT_FOUND = 404
        private const val HTTP_SERVER_ERROR_MIN = 500
        private const val HTTP_SERVER_ERROR_MAX = 599

        private const val BACKEND_KEY_INVALID_PASSWORD = "auth.user.password.invalid"
        private const val BACKEND_KEY_INVALID_CREDENTIALS = "auth.login.invalid_credentials"
        private const val BACKEND_KEY_USER_NOT_FOUND = "user.user.id.not_found"
        private const val BACKEND_KEY_ACCOUNT_BLOCKED = "user.user.status.blocked"
        private const val BACKEND_KEY_TOO_MANY_ATTEMPTS = "auth.throttle.too_many_attempts"
        private const val BACKEND_KEY_EMAIL_NOT_CONFIRMED = "auth.user.email.not_confirmed"
    }
}