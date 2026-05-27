package ru.yeahub.network_api.models

/**
 * DTO роли пользователя:
 * - id - идентификатор роли
 * - name - название роли
 * - permissions - список разрешений роли
 */
data class AuthUserRoleDto(
    val id: Int,
    val name: String,
    val permissions: List<AuthPermissionDto>,
)