package ru.yeahub.impl.domain

class SendResetLinkUseCase(
    private val repository: ForgotPasswordRepository
) {
    suspend operator fun invoke(email: String): ForgotPasswordResult {
        val errorMessage = when {
            email.isBlank() -> "Введите email"
            !email.contains("@") -> "Некорректный email"
            else -> null
        }
        return errorMessage?.let { ForgotPasswordResult.Error(it) }
            ?: repository.sendResetLink(email.trim())
    }
}