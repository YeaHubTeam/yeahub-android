package ru.yeahub.authentication.impl.login.presentation.model

import ru.yeahub.core_utils.common.TextOrResource

/**
 * Состояние формы логина:
 * - email — текущее значение email
 * - password — текущее значение пароля
 * - isPasswordVisible — флаг видимости пароля
 * - emailError — ошибка поля email
 * - passwordError — ошибка поля пароля
 * - isSubmitEnabled — доступность кнопки входа
 */
data class LoginFormState(
    val email: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val emailError: TextOrResource?,
    val passwordError: TextOrResource?,
    val isSubmitEnabled: Boolean,
)

/**
 * Состояние экрана логина:
 * - Content — обычное состояние формы
 * - Loading — состояние загрузки во время авторизации
 */
sealed interface LoginState {
    val formState: LoginFormState

    data class Content(
        override val formState: LoginFormState,
    ) : LoginState

    data class Loading(
        override val formState: LoginFormState,
    ) : LoginState
}