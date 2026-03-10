package ru.yeahub.impl.presentation.state

sealed class ForgotPasswordScreenState {
    data object Initial : ForgotPasswordScreenState()
    data class Content(
        val email: String,
        val isLoading: Boolean,
        val emailError: String?,
        val isSent: Boolean,
    ) : ForgotPasswordScreenState() {
        val isEmailValid: Boolean = emailError == null && email.isNotBlank()
    }
    data class Error(val message: String?) : ForgotPasswordScreenState()
}