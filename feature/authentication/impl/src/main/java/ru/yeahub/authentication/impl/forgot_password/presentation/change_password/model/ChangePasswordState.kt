package ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model

import ru.yeahub.core_utils.common.TextOrResource

/**
 * Состояние экрана "забыл пароль", этап смены пароля:
 * - password — введенный пароль
 * - repeatedPassword — введенное повторение пароля
 * - isPasswordVisible — видимость пароля
 * - isRepeatedPasswordVisible — видимость повторения пароля
 * - passwordError - ошибка в пароле
 * - repeatedPasswordError - ошибка в повторении пароля
 * - isSubmitEnabled - кнопка "сохранить" активна
 * - isSubmitting — идет смена пароля
 */

data class ChangePasswordState(
    val password: String,
    val repeatedPassword: String,
    val isPasswordVisible: Boolean,
    val isRepeatedPasswordVisible: Boolean,
    val passwordError: TextOrResource?,
    val repeatedPasswordError: TextOrResource?,
    val tokenError: TextOrResource?,
    val isSubmitEnabled: Boolean,
    val isSubmitting: Boolean,
)
