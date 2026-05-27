package ru.yeahub.network_api.models

/**
 * DTO разрешения пользователя:
 * - id - идентификатор разрешения
 * - name - название разрешения
 */
data class AuthPermissionDto(
    val id: Int,
    val name: String,
)