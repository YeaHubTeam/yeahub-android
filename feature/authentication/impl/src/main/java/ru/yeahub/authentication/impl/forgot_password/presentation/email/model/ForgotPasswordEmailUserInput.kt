package ru.yeahub.authentication.impl.forgot_password.presentation.email.model

import ru.yeahub.core_utils.common.TextOrResource

/**
 * Пользовательский ввод на экране "забыл пароль", этап ввода мейла:
 * - email — введенный email
 * - isEmailTouched — email уже терял фокус
 * - isValidationRequested — юзер нажимал "Отправить" (отправить мейл)
 * - isSubmitting — идет отправка мейла
 * - isSuccessDialogVisible — отображение модалки "мы отправили вам письмо с инструкциями"
 * - emailServerError — серверная ошибка email
 */

data class ForgotPasswordEmailUserInput(
    val email: String,
    val isEmailTouched: Boolean,
    val isValidationRequested: Boolean,
    val isSubmitting: Boolean,
    val isSuccessDialogVisible: Boolean,
    val emailServerError: TextOrResource?,
    val cooldownSecondsLeft: Int
) {
    val isCooldownActive: Boolean
        get() = cooldownSecondsLeft > 0
}