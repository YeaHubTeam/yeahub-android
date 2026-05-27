package ru.yeahub.authentication.impl.login.domain.entity

/**
 * Токены авторизации:
 * - accessToken - токен доступа для защищённых API-запросов
 */
data class AuthTokens(
    val accessToken: String,
)