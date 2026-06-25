package ru.yeahub.authentication.impl.registration.presentation

import ru.yeahub.core_utils.common.TextOrResource

data class RegistrationFormState(
    val nickname: String,
    val nicknameError: TextOrResource?,
    val email: String,
    val emailError: TextOrResource?,
    val password: String,
    val passwordError: TextOrResource?,
    val confirmPassword: String,
    val confirmPasswordError: TextOrResource?,
    val isPdAccepted: Boolean,
    val isOfferAccepted: Boolean,
    val isMailingAccepted: Boolean,
    val isPasswordVisible: Boolean,
    val isConfirmPasswordVisible: Boolean,
    val isSubmitEnabled: Boolean,
    val isEmailTouched: Boolean,
    val isPasswordTouched: Boolean,
    val isConfirmPasswordTouched: Boolean,
)

sealed interface RegistrationUiState {
    val formState: RegistrationFormState

    data class Content(override val formState: RegistrationFormState) :
        RegistrationUiState

    data class Loading(override val formState: RegistrationFormState) : RegistrationUiState

    data class Error(val message: TextOrResource? = null, override val formState: RegistrationFormState) :
        RegistrationUiState
}

sealed interface RegistrationAction {
    data class NicknameChanged(val value: String) : RegistrationAction
    data class EmailChanged(val value: String) : RegistrationAction
    data class PasswordChanged(val value: String) : RegistrationAction
    data class ConfirmPasswordChanged(val value: String) : RegistrationAction
    data class PdAcceptedChanged(val value: Boolean) : RegistrationAction
    data class OfferAcceptedChanged(val value: Boolean) : RegistrationAction
    data class MailingAcceptedChanged(val value: Boolean) : RegistrationAction
    data object TogglePasswordVisible : RegistrationAction
    data object ToggleConfirmPasswordVisible : RegistrationAction
    data object SubmitClicked : RegistrationAction
    data class EmailFocusChanged(val hasFocus: Boolean) : RegistrationAction
    data class PasswordFocusChanged(val hasFocus: Boolean) : RegistrationAction
    data class ConfirmPasswordFocusChanged(val hasFocus: Boolean) : RegistrationAction
}
