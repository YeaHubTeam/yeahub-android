package ru.yeahub.network_api.models

import com.google.gson.annotations.SerializedName
/**
 * Response модель авторизации:
 * - accessToken - access token пользователя
 * - user - данные пользователя
 */
data class LoginResponseDto(
    @SerializedName("access_token")
    val accessToken: String,
    val user: AuthUserDto,
)