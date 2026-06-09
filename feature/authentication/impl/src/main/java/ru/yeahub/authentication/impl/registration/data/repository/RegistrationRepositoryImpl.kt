package ru.yeahub.authentication.impl.registration.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import ru.yeahub.authentication.impl.registration.data.mapper.RegistrationDomainToDataMapper
import ru.yeahub.authentication.impl.registration.data.repository.remote.RegistrationRemoteDataSourceApi
import ru.yeahub.authentication.impl.registration.domain.entity.Failure
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationError
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationException
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationModel
import ru.yeahub.authentication.impl.registration.domain.repository.RegistrationRepositoryApi
import ru.yeahub.network_api.models.ErrorResponseDto
import java.io.IOException

class RegistrationRepositoryImpl(
    private val remoteDataSourceApi: RegistrationRemoteDataSourceApi,
    private val mapper: RegistrationDomainToDataMapper,
    private val gson: Gson
) : RegistrationRepositoryApi {

    override suspend fun register(registrationModel: RegistrationModel) =
        try {
            val request = mapper.map(registrationModel)
            remoteDataSourceApi.register(request)
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            throw RegistrationException(
                error = RegistrationError.UnknownError,
                failure = Failure(cause = e)
            )
        } catch (e: HttpException) {
            throw mapHttpException(e)
        }

    private fun mapHttpException(e: HttpException): RegistrationException {
        val code = e.code()
        val errorBody = e.response()?.errorBody()?.string()
        val errorDto = errorBody?.let {
            runCatching { gson.fromJson(it, ErrorResponseDto::class.java) }.getOrNull()
        }

        val backendKey = errorDto?.message

        val error = when {
            backendKey == BACKEND_KEY_USER_CONFLICT || code == HTTP_CONFLICT -> RegistrationError.Conflict
            backendKey == BACKEND_KEY_USER_NOT_FOUND || code == HTTP_NOT_FOUND -> RegistrationError.NotFound
            else -> RegistrationError.UnknownError
        }

        return RegistrationException(error = error, failure = Failure(cause = e, httpCode = code))
    }

    private companion object {
        private const val HTTP_CONFLICT = 409
        private const val HTTP_NOT_FOUND = 404
        private const val BACKEND_KEY_USER_CONFLICT = "user.user.conflict"
        private const val BACKEND_KEY_USER_NOT_FOUND = "user.user.id.not_found"
    }
}