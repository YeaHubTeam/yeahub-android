package ru.yeahub.core_utils.validation

/**
 * Типы ошибок валидации пароля.
 */
enum class PasswordValidationError {
    TOO_SHORT,
    NO_UPPERCASE,
    NO_DIGIT,
    NO_SPECIAL_CHAR
}
