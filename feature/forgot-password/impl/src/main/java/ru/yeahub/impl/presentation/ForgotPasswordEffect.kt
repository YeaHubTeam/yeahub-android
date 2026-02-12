package ru.yeahub.impl.presentation

sealed interface ForgotPasswordEffect {
    data class ShowSnackbar(val message: String) : ForgotPasswordEffect
    data object NavigateBack : ForgotPasswordEffect
    data object NavigateToCheckEmail : ForgotPasswordEffect
}