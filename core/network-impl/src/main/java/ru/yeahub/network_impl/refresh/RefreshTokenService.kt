package ru.yeahub.network_impl.refresh

import retrofit2.Call
import retrofit2.http.GET
import ru.yeahub.network_api.models.LoginResponseDto

/**
 * Отдельный Retrofit-сервис для обновления access token.
 *
 * Возвращает Call, потому что OkHttp interceptor работает синхронно.
 * Запрос выполняется через execute() внутри сетевого потока.
 */
interface RefreshTokenService {

    @GET("auth/refresh")
    fun refreshToken(): Call<LoginResponseDto>
}