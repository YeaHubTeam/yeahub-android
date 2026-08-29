package ru.yeahub.authentication.impl.forgot_password.presentation.email.model

import ru.yeahub.core_utils.common.TextOrResource

/**
 * Одноразовые команды экрана "забыл пароль", этап ввода мейла:
 * - NavigateBack — переход обратно на главный экран
 * - ShowSnackbar — показ глобального сообщения
 */

interface ForgotPasswordEmailCommand {
    data object NavigateBack : ForgotPasswordEmailCommand
    data class ShowSnackbar(val message: TextOrResource) : ForgotPasswordEmailCommand
}