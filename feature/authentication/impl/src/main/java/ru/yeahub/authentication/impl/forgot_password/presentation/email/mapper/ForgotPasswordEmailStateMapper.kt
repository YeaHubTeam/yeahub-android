package ru.yeahub.authentication.impl.forgot_password.presentation.email.mapper

import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.forgot_password.presentation.email.model.ForgotPasswordEmailState
import ru.yeahub.authentication.impl.forgot_password.presentation.email.model.ForgotPasswordEmailRawState
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.validation.EmailValidator

class ForgotPasswordEmailStateMapper {

    internal fun getInitialRawState(): ForgotPasswordEmailRawState =
        ForgotPasswordEmailRawState(
            email = "",
            isEmailTouched = false,
            isValidationRequested = false,
            isSubmitting = false,
            isSuccessDialogVisible = false,
            emailServerError = null,
            cooldownSecondsLeft = 0
        )

    internal fun getInitialState(): ForgotPasswordEmailState =
        mapToScreenState(getInitialRawState())

    internal fun mapToScreenState(
        rawState: ForgotPasswordEmailRawState,
    ): ForgotPasswordEmailState {
        val emailLocalError = validateEmail(rawState.email)

        val shouldShowEmailError =
            rawState.isEmailTouched || rawState.isValidationRequested

        return ForgotPasswordEmailState(
            email = rawState.email,
            emailError = rawState.emailServerError ?: emailLocalError.takeIf {
                shouldShowEmailError
            },
            isSubmitEnabled = emailLocalError == null,
            isSubmitting = rawState.isSubmitting,
            isSuccessDialogVisible = rawState.isSuccessDialogVisible,
            cooldownSecondsLeft = rawState.cooldownSecondsLeft
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