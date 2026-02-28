package ru.yeahub.authentication.impl.registration.data.repository

import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import ru.yeahub.authentication.impl.registration.data.mapper.RegistrationDomainToDataMapper
import ru.yeahub.authentication.impl.registration.data.repository.remote.RegistrationRemoteDataSourceApi
import ru.yeahub.authentication.impl.registration.domain.entity.Failure
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationError
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationException
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationModel
import ru.yeahub.authentication.impl.registration.domain.repository.RegistrationRepositoryApi
import java.io.IOException

class RegistrationRepositoryImpl(
    private val remoteDataSourceApi: RegistrationRemoteDataSourceApi,
    private val mapper: RegistrationDomainToDataMapper
) : RegistrationRepositoryApi {

    override suspend fun register(registrationModel: RegistrationModel): Result<Unit> =
        try {
            val request = mapper.map(registrationModel)
            remoteDataSourceApi.register(request)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Result.failure(
                RegistrationException(
                    error = RegistrationError.Network,
                    failure = Failure(cause = e)
                )
            )
        } catch (e: HttpException) {
            Result.failure(mapHttpException(e))
        }

    private fun mapHttpException(e: HttpException): RegistrationException {
        val code = e.code()
        val error =
            when (code) {
                HTTP_CONFLICT -> RegistrationError.EmailAlreadyExists
                HTTP_BAD_REQUEST -> RegistrationError.InvalidCredentials
                in HTTP_SERVER_ERROR_MIN..HTTP_SERVER_ERROR_MAX -> RegistrationError.Server
                else -> RegistrationError.Unknown
            }
        return RegistrationException(error = error, failure = Failure(cause = e, httpCode = code))
    }

    private companion object {
        private const val HTTP_BAD_REQUEST = 400
        private const val HTTP_CONFLICT = 409
        private const val HTTP_SERVER_ERROR_MIN = 500
        private const val HTTP_SERVER_ERROR_MAX = 599
    }
}