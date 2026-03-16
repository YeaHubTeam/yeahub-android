package ru.yeahub.impl.presentation.state

sealed interface ForgotPasswordState {
    data class Content(
        val email: String,
        val emailValidationError: String?,
        val isSuccessDialogVisible: Boolean,
    ) : ForgotPasswordState

    data class Loading(
        val email: String,
        val emailValidationError: String?,
    ) : ForgotPasswordState

    data class Error(
        val email: String,
        val error: String,
        val emailValidationError: String?,
    ) : ForgotPasswordState
}