package ru.yeahub.authentication.impl.forgot_password.domain

class SendResetLinkUseCase(
    private val repository: ForgotPasswordRepository
) {
    suspend operator fun invoke(email: String): ForgotPasswordResult {
        return repository.sendResetLink(email.trim())
    }
}