package ru.yeahub.authentication.impl.login.domain.entity

/**
 * Исключение авторизации:
 * - error - бизнес-тип ошибки
 * - failure - техническая информация
 */
class LoginException(
    val error: LoginError,
    val failure: Failure = Failure(),
) : Exception(failure.cause)