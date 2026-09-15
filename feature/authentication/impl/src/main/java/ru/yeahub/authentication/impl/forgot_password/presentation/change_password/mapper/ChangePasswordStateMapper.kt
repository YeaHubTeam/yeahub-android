package ru.yeahub.authentication.impl.forgot_password.presentation.change_password.mapper

import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model.ChangePasswordState
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model.ChangePasswordRawState
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.validation.PasswordValidationError
import ru.yeahub.core_utils.validation.PasswordValidator
import ru.yeahub.authentication.impl.R

class ChangePasswordStateMapper {

    internal fun getInitialRawState(): ChangePasswordRawState = ChangePasswordRawState(
        password = "",
        repeatedPassword = "",
        isPasswordVisible = false,
        isRepeatedPasswordVisible = false,
        isPasswordTouched = false,
        isRepeatedPasswordTouched = false,
        isValidationRequested = false,
        isSubmitting = false,
        passwordServerError = null,
    )

    internal fun getInitialState(
        tokenError: TextOrResource? = null,
    ): ChangePasswordState =
        mapToScreenState(
            rawState = getInitialRawState(),
            tokenError = tokenError
        )

    internal fun mapToScreenState(
        rawState: ChangePasswordRawState,
        tokenError: TextOrResource? = null
    ): ChangePasswordState {
        val passwordLocalError = validatePassword(rawState.password)

        val repeatedPasswordLocalError = validateRepeatedPassword(
            password = rawState.password,
            repeatedPassword = rawState.repeatedPassword,
        )

        val shouldShowPasswordError =
            rawState.isPasswordTouched || rawState.isValidationRequested

        val shouldShowRepeatedPasswordError =
            rawState.isRepeatedPasswordTouched || rawState.isValidationRequested

        return ChangePasswordState(
            password = rawState.password,
            repeatedPassword = rawState.repeatedPassword,
            isPasswordVisible = rawState.isPasswordVisible,
            isRepeatedPasswordVisible = rawState.isRepeatedPasswordVisible,
            passwordError = rawState.passwordServerError ?: passwordLocalError.takeIf {
                shouldShowPasswordError
            },
            repeatedPasswordError = repeatedPasswordLocalError.takeIf {
                shouldShowRepeatedPasswordError
            },
            tokenError = tokenError,
            isSubmitEnabled = tokenError == null &&
                    passwordLocalError == null &&
                    repeatedPasswordLocalError == null,
            isSubmitting = rawState.isSubmitting,
        )
    }

    private fun validatePassword(password: String): TextOrResource? {
        if (password.isBlank()) {
            return TextOrResource.Resource(R.string.password_placeholder)
        }

        return PasswordValidator.validate(password)
            .firstOrNull()
            ?.toErrorText()
    }

    private fun validateRepeatedPassword(
        password: String,
        repeatedPassword: String,
    ): TextOrResource? {
        return when {
            repeatedPassword.isBlank() -> TextOrResource.Resource(R.string.repeat_password)
            repeatedPassword != password -> TextOrResource.Resource(R.string.passwords_do_not_match)
            else -> null
        }
    }

    private fun PasswordValidationError.toErrorText(): TextOrResource {
        return TextOrResource.Resource(
            when (this) {
                PasswordValidationError.TOO_SHORT -> R.string.password_error_too_short
                PasswordValidationError.NO_UPPERCASE -> R.string.password_error_no_uppercase
                PasswordValidationError.NO_DIGIT -> R.string.password_error_no_digit
                PasswordValidationError.NO_SPECIAL_CHAR -> R.string.password_error_no_special_char
            }
        )
    }
}