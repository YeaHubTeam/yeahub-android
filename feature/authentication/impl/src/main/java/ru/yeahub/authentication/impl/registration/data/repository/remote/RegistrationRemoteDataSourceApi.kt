package ru.yeahub.authentication.impl.registration.data.repository.remote

import ru.yeahub.network_api.models.RegistrationRequestDto

interface RegistrationRemoteDataSourceApi {

    suspend fun register(request: RegistrationRequestDto)
}