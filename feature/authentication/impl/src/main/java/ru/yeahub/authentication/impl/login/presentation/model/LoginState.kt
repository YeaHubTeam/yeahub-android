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
 * Состояния экрана логина:
 * - Initial — начальное состояние
 * - Editing — редактирование без показа локальных ошибок
 * - Validation — редактирование с показом локальных ошибок
 * - Loading — состояние загрузки
 * - ServerError — состояние с серверными ошибками полей
 */
sealed interface LoginState {
    val formState: LoginFormState

    data class Initial(
        override val formState: LoginFormState,
    ) : LoginState

    data class Editing(
        override val formState: LoginFormState,
    ) : LoginState

    data class Validation(
        override val formState: LoginFormState,
    ) : LoginState

    data class Loading(
        override val formState: LoginFormState,
    ) : LoginState

    data class ServerError(
        override val formState: LoginFormState,
    ) : LoginState
}