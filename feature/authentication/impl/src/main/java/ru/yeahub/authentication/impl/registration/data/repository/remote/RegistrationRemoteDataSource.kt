package ru.yeahub.authentication.impl.registration.data.repository.remote

import ru.yeahub.network_api.NetworkProvider
import ru.yeahub.network_api.models.RegistrationRequest

class RegistrationRemoteDataSource(private val apiService: NetworkProvider) : RegistrationRemoteDataSourceApi {
    override suspend fun requestRegistration(request: RegistrationRequest) {
        apiService.apiService.registration(request)
    }
}