package ru.yeahub.authentication.impl.registration.presentation

data class RegistrationFormState(
    val nickname: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPdAccepted: Boolean = false,
    val isOfferAccepted: Boolean = false,
    val isMailingAccepted: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isSubmitEnabled: Boolean = false,
)

sealed interface RegistrationUiState {
    val formState: RegistrationFormState

    data class Content(override val formState: RegistrationFormState = RegistrationFormState()) :
        RegistrationUiState

    data class Loading(override val formState: RegistrationFormState) : RegistrationUiState

    data class Error(val message: String, override val formState: RegistrationFormState) :
        RegistrationUiState
}

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
