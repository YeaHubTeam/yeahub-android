package ru.yeahub.impl.presentation.mapper

import ru.yeahub.impl.presentation.state.ForgotPasswordScreenState
import ru.yeahub.impl.presentation.state.ForgotPasswordState
import ru.yeahub.impl.presentation.validator.EmailValidationResult
import ru.yeahub.impl.presentation.validator.EmailValidator

class ForgotPasswordScreenMapper(
    private val emailValidator: EmailValidator,
) {
    fun getScreenState(state: ForgotPasswordState): ForgotPasswordScreenState {
        return when (state) {
            is ForgotPasswordState.Error -> {
                ForgotPasswordScreenState.Error(message = state.error)
            }

            is ForgotPasswordState.Content -> {
                if (state.email.isBlank() && state.emailValidationError == null) {
                    ForgotPasswordScreenState.Initial
                } else {
                    ForgotPasswordScreenState.Content(
                        email = state.email,
                        isLoading = false,
                        emailError = state.emailValidationError,
                        isSent = state.isSuccessDialogVisible,
                    )
                }
            }

            is ForgotPasswordState.Loading -> {
                ForgotPasswordScreenState.Content(
                    email = state.email,
                    isLoading = true,
                    emailError = state.emailValidationError,
                    isSent = false,
                )
            }
        }
    }

    fun validateEmail(email: String): String? {
        return when (val result = emailValidator.validate(email)) {
            is EmailValidationResult.Valid -> null
            is EmailValidationResult.Empty -> null
            is EmailValidationResult.Invalid -> result.errorMessage
        }
    }

    fun canSubmit(email: String): Boolean {
        return emailValidator.isValid(email)
    }
}
