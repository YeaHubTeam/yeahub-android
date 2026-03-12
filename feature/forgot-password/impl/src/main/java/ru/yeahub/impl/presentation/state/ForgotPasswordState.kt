package ru.yeahub.impl.presentation.state

data class ForgotPasswordState(
    val email: String,
    val isLoading: Boolean,
    val error: String?,
    val emailValidationError: String?,
    val isSuccessDialogVisible: Boolean,
)