package ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model

sealed interface ChangePasswordEvent {
    data class OnPasswordChanged(val value: String) : ChangePasswordEvent
    data class OnRepeatedPasswordChanged(val value: String) : ChangePasswordEvent
    data object OnPasswordFocusLost : ChangePasswordEvent
    data object OnRepeatedPasswordFocusLost : ChangePasswordEvent
    data object OnTogglePasswordVisible : ChangePasswordEvent
    data object OnToggleRepeatedPasswordVisible : ChangePasswordEvent
    data object OnSaveClicked : ChangePasswordEvent
}