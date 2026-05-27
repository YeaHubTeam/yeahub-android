package ru.yeahub.network_api.models

/**
 * Request модель авторизации:
 * - email - email пользователя
 * - password - пароль пользователя
 */
data class LoginRequestDto(
    val email: String,
    val password: String,
)