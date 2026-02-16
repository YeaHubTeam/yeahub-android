package ru.yeahub.impl.domain

class SendResetLinkUseCase(
    private val repository: ForgotPasswordRepository
) {
    suspend operator fun invoke(email: String): ForgotPasswordResult {
        if (email.isBlank()) {
            return ForgotPasswordResult.Error("Введите email")
        } else if (!email.contains("@")) {
            return ForgotPasswordResult.Error("Некорректный email")
        } else {
            return repository.sendResetLink(email.trim())
        }
    }
}