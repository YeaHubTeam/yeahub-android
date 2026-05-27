package ru.yeahub.authentication.impl.login.data.repository.remote

import ru.yeahub.network_api.models.LoginRequestDto
import ru.yeahub.network_api.models.LoginResponseDto

/**
 * Контракт remote data source авторизации:
 * - login - выполняет сетевой запрос авторизации
 */
interface LoginRemoteDataSourceApi {

    suspend fun login(request: LoginRequestDto): LoginResponseDto
}