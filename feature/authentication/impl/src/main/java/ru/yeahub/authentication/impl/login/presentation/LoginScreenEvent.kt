package ru.yeahub.authentication.impl.login.presentation

sealed interface LoginScreenEvent {
    data class OnEmailChanged(val value: String) : LoginScreenEvent
    data class OnPasswordChanged(val value: String) : LoginScreenEvent
    data object OnTogglePasswordVisible : LoginScreenEvent
    data object OnForgotPasswordClick : LoginScreenEvent
    data object OnLoginClick : LoginScreenEvent
    data object OnSignUpClick : LoginScreenEvent
}
