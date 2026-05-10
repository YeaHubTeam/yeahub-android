package ru.yeahub.authentication.impl.login.presentation.model

import ru.yeahub.core_utils.common.TextOrResource

/**
 * Состояние экрана логина:
 * - email — текущее значение email
 * - password — текущее значение пароля
 * - isPasswordVisible — флаг видимости пароля
 * - emailError — ошибка поля email
 * - passwordError — ошибка поля пароля
 * - isSubmitEnabled — доступность кнопки входа
 * - isSubmitting — идет ли отправка формы
 */
data class LoginState(
    val email: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val emailError: TextOrResource?,
    val passwordError: TextOrResource?,
    val isSubmitEnabled: Boolean,
    val isSubmitting: Boolean,
)