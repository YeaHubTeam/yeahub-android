package ru.yeahub.authentication.impl.registration.presentation

import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationError
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationException
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.validation.EmailValidator
import ru.yeahub.core_utils.validation.PasswordValidationError
import ru.yeahub.core_utils.validation.PasswordValidator

class RegistrationUiStateMapper {

    fun getInitialFormState(): RegistrationFormState = RegistrationFormState(
        nickname = "",
        nicknameError = null,
        email = "",
        emailError = null,
        password = "",
        passwordError = null,
        confirmPassword = "",
        confirmPasswordError = null,
        isPdAccepted = false,
        isOfferAccepted = false,
        isMailingAccepted = false,
        isPasswordVisible = false,
        isConfirmPasswordVisible = false,
        isSubmitEnabled = false,
        isEmailTouched = false,
        isPasswordTouched = false,
        isConfirmPasswordTouched = false,
    )

    fun mapToInitialState(): RegistrationUiState {
        return RegistrationUiState.Content(getInitialFormState())
    }

    fun mapToUpdatedState(
        currentState: RegistrationUiState,
        action: RegistrationAction
    ): RegistrationUiState {
        val newForm = updateFormState(currentState.formState, action)
        return RegistrationUiState.Content(validateForm(newForm))
    }

    private fun updateFormState(
        currentForm: RegistrationFormState,
        action: RegistrationAction
    ): RegistrationFormState {
        return when (action) {
            is RegistrationAction.NicknameChanged,
            is RegistrationAction.EmailChanged,
            is RegistrationAction.PasswordChanged,
            is RegistrationAction.ConfirmPasswordChanged -> handleFieldAction(currentForm, action)

            is RegistrationAction.EmailFocusChanged,
            is RegistrationAction.PasswordFocusChanged,
            is RegistrationAction.ConfirmPasswordFocusChanged -> handleFocusAction(currentForm, action)

            is RegistrationAction.PdAcceptedChanged,
            is RegistrationAction.OfferAcceptedChanged,
            is RegistrationAction.MailingAcceptedChanged -> handleConsentAction(currentForm, action)

            is RegistrationAction.TogglePasswordVisible,
            is RegistrationAction.ToggleConfirmPasswordVisible -> handleToggleAction(currentForm, action)

            else -> currentForm
        }
    }

    private fun handleFieldAction(
        form: RegistrationFormState,
        action: RegistrationAction
    ): RegistrationFormState = when (action) {
        is RegistrationAction.NicknameChanged -> form.copy(nickname = action.value)
        is RegistrationAction.EmailChanged -> form.copy(email = action.value)
        is RegistrationAction.PasswordChanged -> form.copy(password = action.value)
        is RegistrationAction.ConfirmPasswordChanged -> form.copy(confirmPassword = action.value)
        else -> form
    }

    private fun handleFocusAction(
        form: RegistrationFormState,
        action: RegistrationAction
    ): RegistrationFormState = when (action) {
        is RegistrationAction.EmailFocusChanged ->
            form.copy(isEmailTouched = !action.hasFocus && form.email.isNotEmpty())
        is RegistrationAction.PasswordFocusChanged ->
            form.copy(isPasswordTouched = !action.hasFocus && form.password.isNotEmpty())
        is RegistrationAction.ConfirmPasswordFocusChanged ->
            form.copy(isConfirmPasswordTouched = !action.hasFocus && form.confirmPassword.isNotEmpty())
        else -> form
    }

    private fun handleConsentAction(
        form: RegistrationFormState,
        action: RegistrationAction
    ): RegistrationFormState = when (action) {
        is RegistrationAction.PdAcceptedChanged -> form.copy(isPdAccepted = action.value)
        is RegistrationAction.OfferAcceptedChanged -> form.copy(isOfferAccepted = action.value)
        is RegistrationAction.MailingAcceptedChanged -> form.copy(isMailingAccepted = action.value)
        else -> form
    }

    private fun handleToggleAction(
        form: RegistrationFormState,
        action: RegistrationAction
    ): RegistrationFormState = when (action) {
        is RegistrationAction.TogglePasswordVisible ->
            form.copy(isPasswordVisible = !form.isPasswordVisible)
        is RegistrationAction.ToggleConfirmPasswordVisible ->
            form.copy(isConfirmPasswordVisible = !form.isConfirmPasswordVisible)
        else -> form
    }

    fun mapToLoadingState(currentState: RegistrationUiState): RegistrationUiState {
        return RegistrationUiState.Loading(currentState.formState)
    }

    fun mapToErrorState(
        currentState: RegistrationUiState,
        exception: RegistrationException
    ): RegistrationUiState {
        val errorResource = mapExceptionToResource(exception)
        return RegistrationUiState.Error(errorResource, currentState.formState)
    }

    fun mapExceptionToResource(exception: RegistrationException): TextOrResource {
        return when (exception.error) {
            RegistrationError.Conflict -> TextOrResource.Resource(R.string.error_user_already_exists)
            RegistrationError.NotFound -> TextOrResource.Resource(R.string.error_resource_not_found)
            else -> TextOrResource.Resource(R.string.login_unknown_error)
        }
    }

    private fun validateForm(form: RegistrationFormState): RegistrationFormState {
        val isEmailValid = EmailValidator.isValid(form.email.trim())
        val passwordErrors = PasswordValidator.validate(form.password)
        val isPassValid = passwordErrors.isEmpty()
        val arePasswordsMatch = form.password == form.confirmPassword && form.confirmPassword.isNotEmpty()
        val isNicknameOk = form.nickname.trim().isNotEmpty()

        val matchError = if (form.isConfirmPasswordTouched && form.confirmPassword.isNotEmpty() && !arePasswordsMatch) {
            TextOrResource.Resource(R.string.error_passwords_not_match)
        } else {
            null
        }

        return form.copy(
            emailError = if (form.isEmailTouched && !isEmailValid) {
                TextOrResource.Resource(R.string.error_email_invalid)
            } else {
                null
            },
            passwordError = getPasswordError(form, passwordErrors) ?: matchError,
            confirmPasswordError = matchError,
            isSubmitEnabled = isNicknameOk && isEmailValid && isPassValid &&
                arePasswordsMatch && form.isPdAccepted && form.isOfferAccepted
        )
    }

    private fun getPasswordError(
        form: RegistrationFormState,
        errors: Set<PasswordValidationError>
    ): TextOrResource? {
        if (!form.isPasswordTouched || form.password.isEmpty() || errors.isEmpty()) {
            return null
        }
        return when (errors.first()) {
            PasswordValidationError.TOO_SHORT -> TextOrResource.Resource(R.string.error_password_too_short)
            PasswordValidationError.NO_UPPERCASE -> TextOrResource.Resource(R.string.error_password_no_uppercase)
            PasswordValidationError.NO_DIGIT -> TextOrResource.Resource(R.string.error_password_no_digit)
            PasswordValidationError.NO_SPECIAL_CHAR -> TextOrResource.Resource(R.string.error_password_no_special_char)
        }
    }
}
