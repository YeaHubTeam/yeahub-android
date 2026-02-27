package ru.yeahub.authentication.impl.login.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class LoginUiState(
    val email: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val emailError: String?,
    val passwordError: String?,
    val isSubmitEnabled: Boolean,
    val isLoading: Boolean,
)