package ru.yeahub.network_api.models

import com.google.gson.annotations.SerializedName

/**
 * Request модель авторизации:
 * - email - email пользователя, который API принимает в поле username
 * - password - пароль пользователя
 */
data class LoginRequestDto(
    @SerializedName("username")
    val email: String,
    val password: String,
)
