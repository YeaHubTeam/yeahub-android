package ru.yeahub.authentication.impl.login.presentation.model

import ru.yeahub.core_utils.common.TextOrResource

/**
 * Источник ошибки поля:
 * - LOCAL — локальная ошибка валидации
 * - SERVER — серверная ошибка
 */
enum class LoginErrorSource {
    LOCAL,
    SERVER,
}

/**
 * Ошибка поля формы:
 * - message — текст ошибки
 */
data class LoginFieldErrorState(
    val message: TextOrResource,
)