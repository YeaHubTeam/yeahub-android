package ru.yeahub.authentication.impl.forgot_password.domain
//
sealed interface ForgotPasswordResult {
    data object Success : ForgotPasswordResult
    data class Error(val message: String) : ForgotPasswordResult
}