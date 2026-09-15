package ru.yeahub.authentication.impl.forgot_password.domain

class ChangePasswordUseCase() {
    suspend operator fun invoke(
        token: String,
        password: String
    ): ForgotPasswordResult {
        return TODO()
    }
}