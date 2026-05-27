package ru.yeahub.authentication.impl.login.domain.entity

/**
 * Бизнес-ошибки авторизации:
 * - InvalidCredentials - неверные данные авторизации
 * - InvalidPassword - неверный пароль
 * - UserNotFound - пользователь не найден
 * - AccountBlocked - аккаунт заблокирован
 * - TooManyAttempts - слишком много попыток входа
 * - EmailNotConfirmed - email не подтверждён
 * - Network - ошибка сети
 * - Server - ошибка сервера
 * - Unknown - неизвестная ошибка
 */
sealed interface LoginError {

    data object InvalidCredentials : LoginError
    data object InvalidPassword : LoginError
    data object UserNotFound : LoginError
    data object AccountBlocked : LoginError
    data object TooManyAttempts : LoginError
    data object EmailNotConfirmed : LoginError
    data object Network : LoginError
    data object Server : LoginError
    data object Unknown : LoginError
}
