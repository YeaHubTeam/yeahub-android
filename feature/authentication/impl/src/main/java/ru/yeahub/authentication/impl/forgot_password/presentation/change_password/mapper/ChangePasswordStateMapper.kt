package ru.yeahub.authentication.impl.forgot_password.presentation.change_password.mapper

import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model.ChangePasswordState
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model.ChangePasswordUserInput
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.validation.PasswordValidationError
import ru.yeahub.core_utils.validation.PasswordValidator
import ru.yeahub.authentication.impl.R

class ChangePasswordStateMapper {

    internal fun getInitialUserInput(): ChangePasswordUserInput = ChangePasswordUserInput(
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
            userInput = getInitialUserInput(),
            tokenError = tokenError
        )

    internal fun mapToScreenState(
        userInput: ChangePasswordUserInput,
        tokenError: TextOrResource? = null
    ): ChangePasswordState {
        val passwordLocalError = validatePassword(userInput.password)

        val repeatedPasswordLocalError = validateRepeatedPassword(
            password = userInput.password,
            repeatedPassword = userInput.repeatedPassword,
        )

        val shouldShowPasswordError =
            userInput.isPasswordTouched || userInput.isValidationRequested

        val shouldShowRepeatedPasswordError =
            userInput.isRepeatedPasswordTouched || userInput.isValidationRequested

        return ChangePasswordState(
            password = userInput.password,
            repeatedPassword = userInput.repeatedPassword,
            isPasswordVisible = userInput.isPasswordVisible,
            isRepeatedPasswordVisible = userInput.isRepeatedPasswordVisible,
            passwordError = userInput.passwordServerError ?: passwordLocalError.takeIf {
                shouldShowPasswordError
            },
            repeatedPasswordError = repeatedPasswordLocalError.takeIf {
                shouldShowRepeatedPasswordError
            },
            tokenError = tokenError,
            isSubmitEnabled = tokenError == null &&
                    passwordLocalError == null &&
                    repeatedPasswordLocalError == null,
            isSubmitting = userInput.isSubmitting,
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