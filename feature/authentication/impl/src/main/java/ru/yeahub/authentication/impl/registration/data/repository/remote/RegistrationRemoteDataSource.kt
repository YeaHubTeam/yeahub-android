package ru.yeahub.authentication.impl.registration.data.repository.remote

import ru.yeahub.network_api.NetworkProvider
import ru.yeahub.network_api.models.RegistrationRequestDto

class RegistrationRemoteDataSource(private val apiService: NetworkProvider) : RegistrationRemoteDataSourceApi {
    override suspend fun register(request: RegistrationRequestDto) {
        apiService.apiService.register(request)
    }
}