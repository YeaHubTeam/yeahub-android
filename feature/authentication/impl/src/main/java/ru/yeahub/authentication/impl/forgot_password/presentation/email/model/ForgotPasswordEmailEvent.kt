package ru.yeahub.authentication.impl.forgot_password.presentation.email.model

/**
 * Действия пользователя на экране "забыл пароль", этап ввода мейла:
 * - OnEmailChanged — изменение email
 * - OnEmailFocusLost — email потерял фокус
 * - OnSubmitClicked — нажатие на кнопку подтверждения
 * - OnBackClicked — нажатие на кнопку "назад"
 * - OnSuccessDialogDismissed — закрытие диалога успешной отправки
 * - OnResendClicked — повторное отправление письма
 *
 */
sealed interface ForgotPasswordEmailEvent {
    data class OnEmailChanged(val value: String) : ForgotPasswordEmailEvent
    data object OnEmailFocusLost : ForgotPasswordEmailEvent
    data object OnSubmitClicked : ForgotPasswordEmailEvent
    data object OnBackClicked : ForgotPasswordEmailEvent
    data object OnSuccessDialogDismissed : ForgotPasswordEmailEvent
    data object OnResendClicked : ForgotPasswordEmailEvent
}
