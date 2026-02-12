package ru.yeahub.impl.presentation

    sealed interface ForgotPasswordIntent {
        data class EmailChanged(val value: String) : ForgotPasswordIntent
        data object SubmitClicked : ForgotPasswordIntent
        data object BackClicked : ForgotPasswordIntent
    }
