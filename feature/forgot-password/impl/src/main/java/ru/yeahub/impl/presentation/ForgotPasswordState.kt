package ru.yeahub.impl.presentation

data class ForgotPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isEmailError: Boolean = false,
    val emailErrorText: String? = null,
    val isSent: Boolean = false
)