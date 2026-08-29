package ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model

sealed interface ChangePasswordAction {
    data class OnPasswordChanged(val value: String) : ChangePasswordAction
    data class OnRepeatedPasswordChanged(val value: String) : ChangePasswordAction
    data object OnPasswordFocusLost : ChangePasswordAction
    data object OnRepeatedPasswordFocusLost : ChangePasswordAction
    data object OnTogglePasswordVisible : ChangePasswordAction
    data object OnToggleRepeatedPasswordVisible : ChangePasswordAction
    data object OnSaveClick : ChangePasswordAction
}