package ru.yeahub.authentication.impl.login.domain.entity

/**
 * Бизнес-ошибки авторизации:
 * - InvalidCredentials - неверные логин/пароль
 * - UserNotFound - пользователь не найден
 * - Network - ошибка сети
 * - Server - ошибка сервера
 * - Unknown - неизвестная ошибка
 */
sealed interface LoginError {

    data object InvalidCredentials : LoginError
    data object UserNotFound : LoginError
    data object Network : LoginError
    data object Server : LoginError
    data object Unknown : LoginError
}
