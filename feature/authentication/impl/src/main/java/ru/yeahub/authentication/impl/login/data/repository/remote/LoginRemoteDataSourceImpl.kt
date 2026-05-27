package ru.yeahub.authentication.impl.login.data.repository.remote

import ru.yeahub.network_api.NetworkProvider
import ru.yeahub.network_api.models.LoginRequestDto
import ru.yeahub.network_api.models.LoginResponseDto

/**
 * Реализация remote data source авторизации:
 * - вызывает методы apiService из NetworkProvider
 */
class LoginRemoteDataSourceImpl(
    private val apiService: NetworkProvider,
) : LoginRemoteDataSourceApi {

    override suspend fun login(request: LoginRequestDto): LoginResponseDto {
        return apiService.apiService.login(request)
    }
}