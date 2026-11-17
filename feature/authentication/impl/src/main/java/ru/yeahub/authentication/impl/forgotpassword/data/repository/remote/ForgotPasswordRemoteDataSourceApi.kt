package ru.yeahub.authentication.impl.forgotpassword.data.repository.remote

import ru.yeahub.network_api.models.ForgotPasswordResponseDto

/**
 * Контракт remote data source восстановления пароля:
 * - resetPassword - выполняет сетевой запрос на изменение пароля (письмо на почту)
 */
interface ForgotPasswordRemoteDataSourceApi {

    suspend fun resetPassword(email: String): ForgotPasswordResponseDto
}