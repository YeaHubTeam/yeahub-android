package ru.yeahub.authentication.impl.login.domain.entity

/**
 * Техническая информация об ошибке:
 * - cause - исходная причина (Throwable)
 * - httpCode - HTTP-код ответа
 */
data class Failure(
    val cause: Throwable? = null,
    val httpCode: Int? = null,
)