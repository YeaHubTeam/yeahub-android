package ru.yeahub.authentication.impl.login.presentation.model

import ru.yeahub.core_utils.common.TextOrResource

/**
 * Одноразовые команды экрана:
 * - NavigateToMain — переход на главный экран
 * - NavigateToSignUp — переход на экран регистрации
 * - NavigateToForgotPassword — переход на экран восстановления пароля
 * - ShowSnackbar — показ глобального сообщения
 */
sealed interface LoginCommand {

    data object NavigateToMain : LoginCommand
    data object NavigateToSignUp : LoginCommand
    data object NavigateToForgotPassword : LoginCommand
    data class ShowSnackbar(val message: TextOrResource) : LoginCommand
}