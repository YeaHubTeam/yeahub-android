package ru.yeahub.authentication.impl.forgot_password.domain
//
interface ForgotPasswordRepository {

    suspend fun sendResetLink(email: String): ForgotPasswordResult
}