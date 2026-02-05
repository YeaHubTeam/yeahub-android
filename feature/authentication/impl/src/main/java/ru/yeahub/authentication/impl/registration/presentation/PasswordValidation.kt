package ru.yeahub.authentication.impl.registration.presentation

private const val MIN_PASSWORD_LENGTH = 8

internal fun isPasswordValid(password: String): Boolean {
    if (password.length < MIN_PASSWORD_LENGTH) return false
    val hasDigit = password.any(Char::isDigit)
    val hasLower = password.any { it in 'a'..'z' }
    val hasUpper = password.any { it in 'A'..'Z' }
    return hasDigit && hasLower && hasUpper
}