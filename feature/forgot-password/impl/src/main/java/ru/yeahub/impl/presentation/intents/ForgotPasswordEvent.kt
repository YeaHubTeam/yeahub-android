package ru.yeahub.impl.presentation.intents

sealed interface ForgotPasswordEvent {
    data class EmailChanged(val value: String) : ForgotPasswordEvent
    data object SubmitClicked : ForgotPasswordEvent
    data object BackClicked : ForgotPasswordEvent
}