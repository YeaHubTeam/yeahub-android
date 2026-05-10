package ru.yeahub.authentication.impl.login.presentation.mapper

import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.login.presentation.model.LoginState
import ru.yeahub.authentication.impl.login.presentation.model.LoginUserInput
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.validation.EmailValidator

/**
 * Mapper состояния логина:
 * - валидирует пользовательский ввод
 * - решает, какие ошибки показывать
 * - собирает LoginState для UI
 */
class LoginStateMapper {

    fun getInitialUserInput(): LoginUserInput = LoginUserInput(
        email = "",
        password = "",
        isPasswordVisible = false,
        isEmailTouched = false,
        isPasswordTouched = false,
        isValidationRequested = false,
        isSubmitting = false,
        emailServerError = null,
        passwordServerError = null,
    )

    fun getInitialState(): LoginState = mapToScreenState(
        userInput = getInitialUserInput(),
    )

    fun mapToScreenState(userInput: LoginUserInput): LoginState {
        val localEmailError = getLocalEmailError(
            email = userInput.email,
        )
        val localPasswordError = getLocalPasswordError(
            password = userInput.password,
        )

        return LoginState(
            email = userInput.email,
            password = userInput.password,
            isPasswordVisible = userInput.isPasswordVisible,
            emailError = getEmailError(
                userInput = userInput,
                localEmailError = localEmailError,
            ),
            passwordError = getPasswordError(
                userInput = userInput,
                localPasswordError = localPasswordError,
            ),
            isSubmitEnabled = isEmailValid(
                email = userInput.email,
            ) && isPasswordValid(
                password = userInput.password,
            ),
            isSubmitting = userInput.isSubmitting,
        )
    }

    private fun getEmailError(
        userInput: LoginUserInput,
        localEmailError: TextOrResource?,
    ): TextOrResource? {
        userInput.emailServerError?.let { serverError ->
            return serverError
        }

        return if (userInput.isEmailTouched || userInput.isValidationRequested) {
            localEmailError
        } else {
            null
        }
    }

    private fun getPasswordError(
        userInput: LoginUserInput,
        localPasswordError: TextOrResource?,
    ): TextOrResource? {
        userInput.passwordServerError?.let { serverError ->
            return serverError
        }

        return if (userInput.isPasswordTouched || userInput.isValidationRequested) {
            localPasswordError
        } else {
            null
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && EmailValidator.isValid(email)
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty()
    }

    private fun getLocalEmailError(email: String): TextOrResource? {
        return when {
            email.isEmpty() -> null
            EmailValidator.isValid(email) -> null
            else -> TextOrResource.Resource(R.string.login_email_invalid)
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