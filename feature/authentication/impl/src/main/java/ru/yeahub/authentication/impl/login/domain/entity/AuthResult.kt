package ru.yeahub.authentication.impl.login.domain.entity

/**
 * Результат успешной авторизации:
 * - tokens - токены авторизации
 * - userProfile - профиль авторизованного пользователя
 */
data class AuthResult(
    val tokens: AuthTokens,
    val userProfile: UserProfile,
)