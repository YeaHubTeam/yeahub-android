package ru.yeahub.authentication.impl.login.presentation.model

/**
 * Действия пользователя на экране логина:
 * - OnEmailChanged — изменение email
 * - OnPasswordChanged — изменение пароля
 * - OnTogglePasswordVisible — переключение видимости пароля
 * - OnForgotPasswordClick — нажатие на "Забыли пароль?"
 * - OnLoginClick — нажатие на кнопку входа
 * - OnSignUpClick — нажатие на кнопку регистрации
 */
sealed interface LoginAction {

    data class OnEmailChanged(val value: String) : LoginAction
    data class OnPasswordChanged(val value: String) : LoginAction
    data object OnTogglePasswordVisible : LoginAction
    data object OnForgotPasswordClick : LoginAction
    data object OnLoginClick : LoginAction
    data object OnSignUpClick : LoginAction
}