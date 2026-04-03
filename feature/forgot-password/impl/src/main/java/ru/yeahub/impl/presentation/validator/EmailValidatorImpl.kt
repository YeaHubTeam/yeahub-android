package ru.yeahub.impl.presentation.validator

import android.util.Patterns

class EmailValidatorImpl : EmailValidator {

    override fun validate(email: String): EmailValidationResult {
        val trimmedEmail = email.trim()

        return when {
            trimmedEmail.isBlank() -> EmailValidationResult.Empty
            !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> {
                EmailValidationResult.Invalid("Введите корректный email")
            }

            else -> EmailValidationResult.Valid
        }
    }

    override fun isValid(email: String): Boolean {
        return validate(email) is EmailValidationResult.Valid
    }
}