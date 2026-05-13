package ru.yeahub.authentication.impl.login.presentation.model

import ru.yeahub.core_utils.common.TextOrResource

/**
 * Пользовательский ввод на экране логина:
 * - email — введенный email
 * - password — введенный пароль
 * - isPasswordVisible — видимость пароля
 * - isEmailTouched — email уже терял фокус
 * - isPasswordTouched — password уже терял фокус
 * - isValidationRequested — пользователь нажимал "Войти"
 * - isSubmitting — идет отправка формы
 * - emailServerError — серверная ошибка email
 * - passwordServerError — серверная ошибка password
 */
data class LoginUserInput(
    val email: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val isEmailTouched: Boolean,
    val isPasswordTouched: Boolean,
    val isValidationRequested: Boolean,
    val isSubmitting: Boolean,
    val emailServerError: TextOrResource?,
    val passwordServerError: TextOrResource?,
)