package ru.yeahub.authentication.impl.login.presentation.mapper

import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.login.presentation.model.LoginFormState
import ru.yeahub.authentication.impl.login.presentation.model.LoginRawState
import ru.yeahub.authentication.impl.login.presentation.model.LoginState
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.validation.EmailValidator

/**
 * Mapper состояния логина:
 * - создает начальное состояние
 * - валидирует поля
 * - собирает LoginState для UI
 */
class LoginStateMapper {

    fun getInitialRawState(): LoginRawState = LoginRawState(
        email = "",
        password = "",
        isPasswordVisible = false,
        emailServerError = null,
        passwordServerError = null,
        isLoading = false,
        isValidationEnabled = false,
    )

    fun getInitialState(): LoginState = mapToScreenState(
        rawState = getInitialRawState(),
    )

    fun mapToScreenState(rawState: LoginRawState): LoginState {
        val localEmailError = getLocalEmailError(
            email = rawState.email,
        )
        val localPasswordError = getLocalPasswordError(
            password = rawState.password,
        )

        val emailError = when {
            rawState.emailServerError != null -> rawState.emailServerError.message
            rawState.isValidationEnabled -> localEmailError
            else -> null
        }

        val passwordError = when {
            rawState.passwordServerError != null -> rawState.passwordServerError.message
            rawState.isValidationEnabled -> localPasswordError
            else -> null
        }

        val isSubmitEnabled = localEmailError == null && localPasswordError == null

        val formState = LoginFormState(
            email = rawState.email,
            password = rawState.password,
            isPasswordVisible = rawState.isPasswordVisible,
            emailError = emailError,
            passwordError = passwordError,
            isSubmitEnabled = isSubmitEnabled,
        )

        return if (rawState.isLoading) {
            LoginState.Loading(formState = formState)
        } else {
            LoginState.Content(formState = formState)
        }
    }

    private fun getLocalEmailError(email: String): TextOrResource? {
        if (email.isEmpty()) {
            return null
        }

        return if (EmailValidator.isValid(email)) {
            null
        } else {
            TextOrResource.Resource(R.string.login_email_invalid)
        }
    }

    private fun getLocalPasswordError(password: String): TextOrResource? {
        return if (password.isEmpty()) {
            TextOrResource.Resource(R.string.login_password_empty)
        } else {
            null
        }
    }
}