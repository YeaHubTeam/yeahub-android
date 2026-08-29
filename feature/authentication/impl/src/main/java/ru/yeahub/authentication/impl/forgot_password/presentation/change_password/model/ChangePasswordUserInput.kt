package ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model

import ru.yeahub.core_utils.common.TextOrResource

/**
 * Пользовательский ввод на экране "забыл пароль", этап смены пароля:
 * - password — введенный пароль
 * - repeatedPassword — введенное повторение пароля
 * - isPasswordVisible — видимость пароля
 * - isRepeatedPasswordVisible — видимость повторения пароля
 * - isPasswordTouched — пароль уже терял фокус
 * - isRepeatedPasswordTouched — повторение пароля уже теряло фокус
 * - isValidationRequested — юзер нажимал "Сохранить"
 * - isSubmitting — идет смена пароля
 * - passwordServerError — серверная ошибка пароля
 */
data class ChangePasswordUserInput(
    val password: String,
    val repeatedPassword: String,
    val isPasswordVisible: Boolean,
    val isRepeatedPasswordVisible: Boolean,
    val isPasswordTouched: Boolean,
    val isRepeatedPasswordTouched: Boolean,
    val isValidationRequested: Boolean,
    val isSubmitting: Boolean,
    val passwordServerError: TextOrResource?
)
