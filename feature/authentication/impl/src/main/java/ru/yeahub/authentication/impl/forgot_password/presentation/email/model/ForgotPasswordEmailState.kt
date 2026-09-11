package ru.yeahub.authentication.impl.forgot_password.presentation.email.model

import ru.yeahub.core_utils.common.TextOrResource

/**
 * Состояние экрана "забыл пароль", этап ввода мейла:
 * - email — текущее значение email
 * - emailError — ошибка поля email
 * - isSubmitEnabled — доступность кнопки входа
 * - isSubmitting — идет ли отправка формы
 * - isSuccessDialogVisible - — отображение модалки "мы отправили вам письмо с инструкциями"
 */

data class ForgotPasswordEmailState(
    val email: String,
    val emailError: TextOrResource?,
    val isSubmitEnabled: Boolean,
    val isSubmitting: Boolean,
    val isSuccessDialogVisible: Boolean,
    val cooldownSecondsLeft: Int
) {
    val isCooldownActive: Boolean
        get() = cooldownSecondsLeft > 0
}