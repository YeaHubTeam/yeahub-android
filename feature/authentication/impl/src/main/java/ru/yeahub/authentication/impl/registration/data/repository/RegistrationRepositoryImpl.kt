package ru.yeahub.authentication.impl.registration.data.repository

import ru.yeahub.authentication.impl.registration.data.mapper.RegistrationDomainToDataMapper
import ru.yeahub.authentication.impl.registration.data.repository.remote.RegistrationRemoteDataSourceApi
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationModel
import ru.yeahub.authentication.impl.registration.domain.repository.RegistrationRepositoryApi

class RegistrationRepositoryImpl(
    val remoteDataSourceApi: RegistrationRemoteDataSourceApi,
    val mapper: RegistrationDomainToDataMapper
): RegistrationRepositoryApi {

    override suspend fun register(registrationModel: RegistrationModel): Result<Unit> {
        try {
            val map = mapper.map(registrationModel)
            remoteDataSourceApi.requestRegistration(map)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}