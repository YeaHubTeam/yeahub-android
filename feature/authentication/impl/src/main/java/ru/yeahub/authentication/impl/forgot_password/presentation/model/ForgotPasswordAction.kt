package ru.yeahub.authentication.impl.forgot_password.presentation.model

sealed interface ForgotPasswordAction {
    data class OnEmailChanged(val email: String) : ForgotPasswordAction
    data object OnButtonClick : ForgotPasswordAction
    data object OnCloseDialogClick : ForgotPasswordAction
}