package ru.yeahub.impl.presentation.validator

interface EmailValidator {
    fun validate(email: String): EmailValidationResult
    fun isValid(email: String): Boolean
}