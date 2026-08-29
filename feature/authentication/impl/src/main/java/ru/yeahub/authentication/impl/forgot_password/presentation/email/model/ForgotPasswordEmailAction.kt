package ru.yeahub.authentication.impl.forgot_password.presentation.email.model

/**
 * Действия пользователя на экране "забыл пароль", этап ввода мейла:
 * - OnEmailChanged — изменение email
 * - OnEmailFocusLost — email потерял фокус
 * - OnSubmitClick — нажатие на кнопку подтверждения
 * - OnBackClick — нажатие на кнопку "назад"
 * - OnDismissSuccessDialog — закрытие диалога успешной отправки
 * - OnResendClick — повторное отправление письма
 *
 */
sealed interface ForgotPasswordEmailAction {
    data class OnEmailChanged(val value: String) : ForgotPasswordEmailAction
    data object OnEmailFocusLost : ForgotPasswordEmailAction
    data object OnSubmitClick : ForgotPasswordEmailAction
    data object OnBackClick : ForgotPasswordEmailAction
    data object OnDismissSuccessDialog : ForgotPasswordEmailAction
    data object OnResendClick : ForgotPasswordEmailAction
}
