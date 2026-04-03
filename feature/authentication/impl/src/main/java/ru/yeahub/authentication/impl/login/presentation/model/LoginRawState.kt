package ru.yeahub.authentication.impl.login.presentation.model

/**
 * Внутреннее состояние ViewModel:
 * - email — текущее значение email
 * - password — текущее значение пароля
 * - isPasswordVisible — флаг видимости пароля
 * - emailServerError — серверная ошибка поля email
 * - passwordServerError — серверная ошибка поля пароля
 * - isLoading — флаг выполнения запроса
 * - isValidationEnabled — флаг показа локальных ошибок
 */
data class LoginRawState(
    val email: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val emailServerError: LoginFieldErrorState?,
    val passwordServerError: LoginFieldErrorState?,
    val isLoading: Boolean,
    val isValidationEnabled: Boolean,
)