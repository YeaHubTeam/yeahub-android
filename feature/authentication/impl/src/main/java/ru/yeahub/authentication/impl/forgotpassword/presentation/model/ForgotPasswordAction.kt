package ru.yeahub.authentication.impl.forgotpassword.presentation.model

sealed interface ForgotPasswordAction {
    data class OnEmailChanged(val email: String) : ForgotPasswordAction
    data object OnButtonClick : ForgotPasswordAction
    data object OnBackToAuthClick : ForgotPasswordAction

    data object  OnReSendEmailClick : ForgotPasswordAction
}