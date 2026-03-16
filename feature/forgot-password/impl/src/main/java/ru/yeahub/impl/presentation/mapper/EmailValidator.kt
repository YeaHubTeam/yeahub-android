package ru.yeahub.impl.presentation.mapper

import android.util.Patterns

sealed interface EmailValidationResult {
    data object Valid : EmailValidationResult
    data object Empty : EmailValidationResult
    data class Invalid(val errorMessage: String) : EmailValidationResult
}

class EmailValidator {

    fun validate(email: String): EmailValidationResult {
        val trimmedEmail = email.trim()

        return when {
            trimmedEmail.isBlank() -> EmailValidationResult.Empty
            !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> {
                EmailValidationResult.Invalid("Введите корректный email")
            }
            else -> EmailValidationResult.Valid
        }
    }

    fun isValid(email: String): Boolean {
        return validate(email) is EmailValidationResult.Valid
    }
}