package ru.yeahub.authentication.impl.forgotpassword.presentation

data class ForgotPasswordState(
    val email: String = "",
    val isSubmitEnabled: Boolean = false,
    val isSubmitting: Boolean = false,
    val isSuccessDialogVisible: Boolean = false,
)