package ru.yeahub.authentication.impl.login.domain.entity

/**
 * Профиль авторизованного пользователя:
 * - id - идентификатор пользователя
 * - email - email пользователя
 * - username - имя пользователя
 * - avatarUrl - ссылка на аватар пользователя
 */
data class UserProfile(
    val id: String?,
    val email: String?,
    val username: String?,
    val avatarUrl: String?,
)