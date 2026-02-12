package ru.yeahub.impl.domain

sealed interface ForgotPasswordResult {
    data object Success : ForgotPasswordResult
    data class Error(val message: String) : ForgotPasswordResult
}