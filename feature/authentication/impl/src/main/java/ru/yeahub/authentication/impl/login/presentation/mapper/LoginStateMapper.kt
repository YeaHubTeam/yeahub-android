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

    fun getInitialRawState(): LoginRawState = LoginRawState.Initial(
        email = "",
        password = "",
        isPasswordVisible = false,
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

        val formState = when (rawState) {
            is LoginRawState.Initial -> createFormState(
                rawState = rawState,
                emailError = null,
                passwordError = null,
                localEmailError = localEmailError,
                localPasswordError = localPasswordError,
            )

            is LoginRawState.Editing -> createFormState(
                rawState = rawState,
                emailError = null,
                passwordError = null,
                localEmailError = localEmailError,
                localPasswordError = localPasswordError,
            )

            is LoginRawState.Validation -> createFormState(
                rawState = rawState,
                emailError = localEmailError,
                passwordError = localPasswordError,
                localEmailError = localEmailError,
                localPasswordError = localPasswordError,
            )

            is LoginRawState.Loading -> createFormState(
                rawState = rawState,
                emailError = localEmailError,
                passwordError = localPasswordError,
                localEmailError = localEmailError,
                localPasswordError = localPasswordError,
            )

            is LoginRawState.ServerError -> createFormState(
                rawState = rawState,
                emailError = rawState.emailError?.message,
                passwordError = rawState.passwordError?.message,
                localEmailError = localEmailError,
                localPasswordError = localPasswordError,
            )
        }

        return when (rawState) {
            is LoginRawState.Initial -> LoginState.Initial(formState = formState)
            is LoginRawState.Editing -> LoginState.Editing(formState = formState)
            is LoginRawState.Validation -> LoginState.Validation(formState = formState)
            is LoginRawState.Loading -> LoginState.Loading(formState = formState)
            is LoginRawState.ServerError -> LoginState.ServerError(formState = formState)
        }
    }

    private fun createFormState(
        rawState: LoginRawState,
        emailError: TextOrResource?,
        passwordError: TextOrResource?,
        localEmailError: TextOrResource?,
        localPasswordError: TextOrResource?,
    ): LoginFormState = LoginFormState(
        email = rawState.email,
        password = rawState.password,
        isPasswordVisible = rawState.isPasswordVisible,
        emailError = emailError,
        passwordError = passwordError,
        isSubmitEnabled = localEmailError == null && localPasswordError == null,
    )

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