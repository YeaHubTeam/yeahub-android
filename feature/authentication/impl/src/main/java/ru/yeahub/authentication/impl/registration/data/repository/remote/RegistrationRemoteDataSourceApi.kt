package ru.yeahub.authentication.impl.registration.data.repository.remote

import ru.yeahub.network_api.models.RegistrationRequest

interface RegistrationRemoteDataSourceApi {

    suspend fun register(request: RegistrationRequest)
}