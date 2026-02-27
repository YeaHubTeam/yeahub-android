package ru.yeahub.authentication.impl.login.presentation

sealed interface LoginAction {
    data class EmailChanged(val value: String) : LoginAction
    data class PasswordChanged(val value: String) : LoginAction
    data object TogglePasswordVisible : LoginAction
    data object ForgotPasswordClick : LoginAction
    data object LoginClick : LoginAction
    data object TelegramClick : LoginAction
    data object SignUpClick : LoginAction
}