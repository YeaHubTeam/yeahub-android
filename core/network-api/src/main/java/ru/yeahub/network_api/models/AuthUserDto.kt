package ru.yeahub.network_api.models

/**
 * DTO пользователя из auth response:
 * - id - идентификатор пользователя
 * - username - имя пользователя
 * - phone - телефон
 * - email - email
 * - country - страна
 * - city - город
 * - address - адрес
 * - avatarUrl - ссылка на аватар
 * - birthday - дата рождения
 * - updatedAt - дата обновления
 * - createdAt - дата создания
 * - userRoles - роли пользователя
 * - isVerified - подтверждён ли пользователь
 * - isEmailNotificationsEnable - включены ли email-уведомления
 */
data class AuthUserDto(
    val id: String?,
    val username: String?,
    val phone: String?,
    val email: String?,
    val country: String?,
    val city: String?,
    val address: String?,
    val avatarUrl: String?,
    val birthday: String?,
    val updatedAt: String?,
    val createdAt: String?,
    val userRoles: List<AuthUserRoleDto>?,
    val isVerified: Boolean?,
    val isEmailNotificationsEnable: Boolean?,
)