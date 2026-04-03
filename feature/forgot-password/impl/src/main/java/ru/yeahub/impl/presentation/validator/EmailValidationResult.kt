package ru.yeahub.impl.presentation.validator

sealed interface EmailValidationResult {
    data object Valid : EmailValidationResult
    data object Empty : EmailValidationResult
    data class Invalid(val errorMessage: String) : EmailValidationResult
}