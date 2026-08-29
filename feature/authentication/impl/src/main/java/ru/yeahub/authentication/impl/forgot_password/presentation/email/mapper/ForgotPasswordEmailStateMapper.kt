package ru.yeahub.authentication.impl.forgot_password.presentation.email.mapper

import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.forgot_password.presentation.email.model.ForgotPasswordEmailState
import ru.yeahub.authentication.impl.forgot_password.presentation.email.model.ForgotPasswordEmailUserInput
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.validation.EmailValidator

class ForgotPasswordEmailStateMapper {

    internal fun getInitialUserInput(): ForgotPasswordEmailUserInput =
        ForgotPasswordEmailUserInput(
            email = "",
            isEmailTouched = false,
            isValidationRequested = false,
            isSubmitting = false,
            isSuccessDialogVisible = false,
            emailServerError = null,
            cooldownSecondsLeft = 0
        )

    internal fun getInitialState(): ForgotPasswordEmailState =
        mapToScreenState(getInitialUserInput())

    internal fun mapToScreenState(
        userInput: ForgotPasswordEmailUserInput,
    ): ForgotPasswordEmailState {
        val emailLocalError = validateEmail(userInput.email)

        val shouldShowEmailError =
            userInput.isEmailTouched || userInput.isValidationRequested

        return ForgotPasswordEmailState(
            email = userInput.email,
            emailError = userInput.emailServerError ?: emailLocalError.takeIf {
                shouldShowEmailError
            },
            isSubmitEnabled = emailLocalError == null,
            isSubmitting = userInput.isSubmitting,
            isSuccessDialogVisible = userInput.isSuccessDialogVisible,
            cooldownSecondsLeft = userInput.cooldownSecondsLeft
        )
    }

    private fun validateEmail(email: String): TextOrResource? {
        return when {
            email.isBlank() -> TextOrResource.Resource(R.string.email_placeholder)
            EmailValidator.isValid(email) -> null
            else -> TextOrResource.Resource(R.string.login_email_invalid)
        }
    }
}