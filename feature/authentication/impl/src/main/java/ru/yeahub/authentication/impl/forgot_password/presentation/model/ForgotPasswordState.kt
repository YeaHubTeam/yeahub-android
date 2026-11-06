package ru.yeahub.authentication.impl.forgot_password.presentation.model

data class ForgotPasswordState(
    val email: String = "",
    val isSubmitEnabled: Boolean = false,
    val isSubmitting: Boolean = false,
    val isSuccessDialogVisible: Boolean = false,
)