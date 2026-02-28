package ru.yeahub.authentication.impl.registration.data.repository

import java.io.IOException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import ru.yeahub.authentication.impl.registration.data.mapper.RegistrationDomainToDataMapper
import ru.yeahub.authentication.impl.registration.data.repository.remote.RegistrationRemoteDataSourceApi
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationException
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationModel
import ru.yeahub.authentication.impl.registration.domain.repository.RegistrationRepositoryApi

class RegistrationRepositoryImpl(
        val remoteDataSourceApi: RegistrationRemoteDataSourceApi,
        val mapper: RegistrationDomainToDataMapper
) : RegistrationRepositoryApi {

    override suspend fun register(registrationModel: RegistrationModel): Result<Unit> {
        return try {
            val request = mapper.map(registrationModel)
            remoteDataSourceApi.register(request)
            Result.success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            val domainException =
                    when (e) {
                        is IOException -> RegistrationException.NetworkError()
                        is HttpException -> {
                            when (e.code()) {
                                409 -> RegistrationException.EmailAlreadyExists()
                                400 -> RegistrationException.InvalidCredentials()
                                in 500..599 -> RegistrationException.ServerError()
                                else -> RegistrationException.UnknownError(e)
                            }
                        }
                        else -> RegistrationException.UnknownError(e)
                    }
            Result.failure(domainException)
        }
    }
}
