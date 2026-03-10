package ru.yeahub.authentication.impl.registration.presentation

data class RegistrationUiState(
    val nickname: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val isPdAccepted: Boolean,
    val isOfferAccepted: Boolean,
    val isMailingAccepted: Boolean,
    val isPasswordVisible: Boolean,
    val isConfirmPasswordVisible: Boolean,
    val isSubmitEnabled: Boolean,
    val isLoading: Boolean,
    val error: String?,
)

sealed class RegistrationAction {
    data class NicknameChanged(val value: String) : RegistrationAction()
    data class EmailChanged(val value: String) : RegistrationAction()
    data class PasswordChanged(val value: String) : RegistrationAction()
    data class ConfirmPasswordChanged(val value: String) : RegistrationAction()
    data class PdAcceptedChanged(val value: Boolean) : RegistrationAction()
    data class OfferAcceptedChanged(val value: Boolean) : RegistrationAction()
    data class MailingAcceptedChanged(val value: Boolean) : RegistrationAction()
    data object TogglePasswordVisible : RegistrationAction()
    data object ToggleConfirmPasswordVisible : RegistrationAction()
    data object SubmitClicked : RegistrationAction()
}