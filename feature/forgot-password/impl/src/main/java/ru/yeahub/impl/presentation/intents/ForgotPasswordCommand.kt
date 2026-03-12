package ru.yeahub.impl.presentation.intents

sealed interface ForgotPasswordCommand {
    data class ShowSnackbar(val message: String) : ForgotPasswordCommand
    data object NavigateBack : ForgotPasswordCommand
    data object NavigateToCheckEmail : ForgotPasswordCommand
}