package ru.yeahub.authentication.impl.registration.presentation

private const val MIN_PASSWORD_LENGTH = 8

internal fun isPasswordValid(password: String): Boolean {
    val isValid = password.matches(
        Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{$MIN_PASSWORD_LENGTH,}$")
    )
    return isValid
}