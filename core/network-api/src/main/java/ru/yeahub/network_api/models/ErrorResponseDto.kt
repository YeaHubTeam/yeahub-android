package ru.yeahub.network_api.models

/**
 * Response модель ошибки backend:
 * - key - backend key ошибки
 * - message - текстовое описание ошибки
 */
data class ErrorResponseDto(
    val key: String?,
    val message: String?,
)