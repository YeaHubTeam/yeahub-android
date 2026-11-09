package ru.yeahub.authentication.impl.forgotpassword.presentation.model

/**
 * Одноразовые команды экрана:
 * - NavigateToSignIn — переход на экран авторизации
 * - NavigateToForgotPassword — переход на экран восстановления пароля
 */
sealed class ForgotPasswordCommand {

    data object NavigateToSignIn : ForgotPasswordCommand()
}