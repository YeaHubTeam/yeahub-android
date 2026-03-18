package ru.yeahub.authentication.impl.registration.presentation

import android.util.Patterns

class RegistrationUiStateMapper {

    fun getInitialFormState(): RegistrationFormState = RegistrationFormState(
        nickname = "",
        email = "",
        password = "",
        confirmPassword = "",
        isPdAccepted = false,
        isOfferAccepted = false,
        isMailingAccepted = false,
        isPasswordVisible = false,
        isConfirmPasswordVisible = false,
        isSubmitEnabled = false,
    )

    fun getInitialState(): RegistrationUiState = RegistrationUiState.Content(getInitialFormState())

    fun getScreenState(
        formState: RegistrationFormState,
        isLoading: Boolean,
        errorMessage: String?
    ): RegistrationUiState {
        val validatedForm = formState.revalidate()
        return when {
            isLoading -> RegistrationUiState.Loading(validatedForm)
            errorMessage != null -> RegistrationUiState.Error(errorMessage, validatedForm)
            else -> RegistrationUiState.Content(validatedForm)
        }
    }

    private fun RegistrationFormState.revalidate(): RegistrationFormState {
        val nicknameOk = nickname.trim().isNotEmpty()
        val emailOk = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
        val passOk = isPasswordValid(password)
        val confirmOk = password == confirmPassword && confirmPassword.isNotEmpty()
        val requiredConsentsOk = isPdAccepted && isOfferAccepted

        return copy(
            isSubmitEnabled = nicknameOk && emailOk && passOk && confirmOk && requiredConsentsOk
        )
    }
}
