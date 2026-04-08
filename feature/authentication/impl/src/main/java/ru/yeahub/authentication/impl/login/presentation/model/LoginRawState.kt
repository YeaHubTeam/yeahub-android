package ru.yeahub.authentication.impl.login.presentation.model

/**
 * Внутренние состояния логина:
 * - Initial — начальное состояние
 * - Editing — редактирование без показа локальных ошибок
 * - Validation — редактирование с показом локальных ошибок
 * - Loading — состояние загрузки
 * - ServerError — состояние с серверными ошибками полей
 */
sealed interface LoginRawState {
    val email: String
    val password: String
    val isPasswordVisible: Boolean

    data class Initial(
        override val email: String,
        override val password: String,
        override val isPasswordVisible: Boolean,
    ) : LoginRawState

    data class Editing(
        override val email: String,
        override val password: String,
        override val isPasswordVisible: Boolean,
    ) : LoginRawState

    data class Validation(
        override val email: String,
        override val password: String,
        override val isPasswordVisible: Boolean,
    ) : LoginRawState

    data class Loading(
        override val email: String,
        override val password: String,
        override val isPasswordVisible: Boolean,
    ) : LoginRawState

    data class ServerError(
        override val email: String,
        override val password: String,
        override val isPasswordVisible: Boolean,
        val emailError: LoginFieldErrorState?,
        val passwordError: LoginFieldErrorState?,
    ) : LoginRawState
}