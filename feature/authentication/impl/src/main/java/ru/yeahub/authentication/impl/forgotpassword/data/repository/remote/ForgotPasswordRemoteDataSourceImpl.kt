package ru.yeahub.authentication.impl.forgotpassword.data.repository.remote

import ru.yeahub.network_api.NetworkProvider
import ru.yeahub.network_api.models.ForgotPasswordResponseDto

/**
 * Реализация remote data source восстановления пароля:
 * - вызывает соответствующий метод apiService из NetworkProvider
 */
class ForgotPasswordRemoteDataSourceImpl(
    private val apiService: NetworkProvider,
) : ForgotPasswordRemoteDataSourceApi {

    override suspend fun resetPassword(email: String): ForgotPasswordResponseDto {
        return apiService.apiService.resetPassword(email)
    }
}