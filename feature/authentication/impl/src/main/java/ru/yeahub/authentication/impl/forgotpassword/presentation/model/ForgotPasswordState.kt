package ru.yeahub.authentication.impl.forgotpassword.presentation.model

/**
 * Состояние экрана "забыл пароль", этап ввода мейла:
 * - email — текущее значение email
 * - isSubmitEnabled — доступность кнопки входа
 * - isSubmitting — идет ли отправка формы
 * - isSuccessDialogVisible - — отображение диалога "мы отправили вам письмо с инструкциями"
 */
data class ForgotPasswordState(
    val email: String,
    val isSubmitEnabled: Boolean,
    val isSubmitting: Boolean,
    val isSuccessDialogVisible: Boolean,
)