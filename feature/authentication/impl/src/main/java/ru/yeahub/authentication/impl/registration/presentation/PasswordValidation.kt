package ru.yeahub.authentication.impl.registration.presentation

internal fun isPasswordValid(password: String): Boolean {
    if (password.length < 8) return false
    val hasDigit = password.any(Char::isDigit)
    val hasLower = password.any { it in 'a'..'z' }
    val hasUpper = password.any { it in 'A'..'Z' }
    return hasDigit && hasLower && hasUpper
}