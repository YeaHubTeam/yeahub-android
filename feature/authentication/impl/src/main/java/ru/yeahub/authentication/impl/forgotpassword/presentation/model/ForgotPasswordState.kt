package ru.yeahub.authentication.impl.forgotpassword.presentation.model

data class ForgotPasswordState(
    val email: String = "",
    val isSubmitEnabled: Boolean = false,
    val isSubmitting: Boolean = false,
    val isSuccessDialogVisible: Boolean = false,
)