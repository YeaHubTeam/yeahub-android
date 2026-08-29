package ru.yeahub.authentication.impl.forgot_password.domain

class ChangePasswordUseCase(
//    private val repository: ForgotPasswordRepository
) {
    // TODO: сделать репо in constructor под смену пароля
    // TODO ChangePasswordUseCase(token, password): ForgotPasswordResult
    suspend operator fun invoke(
        token: String,
        password: String
    ): ForgotPasswordResult {
        return TODO()
    }
}