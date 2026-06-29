package ru.yeahub.core_utils.validation

/**
 * Универсальный валидатор паролей для проекта YeaHub.
 */
object PasswordValidator {

    private const val MIN_PASSWORD_LENGTH = 8

    /**
     * Проверяет пароль на соответствие требованиям безопасности.
     * Возвращает набор PasswordValidationError, если требования не выполнены.
     */
    fun validate(password: String): Set<PasswordValidationError> = buildSet {
        if (password.length < MIN_PASSWORD_LENGTH) {
            add(PasswordValidationError.TOO_SHORT)
        }
        if (!password.any { it.isUpperCase() }) {
            add(PasswordValidationError.NO_UPPERCASE)
        }
        if (!password.any { it.isDigit() }) {
            add(PasswordValidationError.NO_DIGIT)
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            add(PasswordValidationError.NO_SPECIAL_CHAR)
        }
    }
}
