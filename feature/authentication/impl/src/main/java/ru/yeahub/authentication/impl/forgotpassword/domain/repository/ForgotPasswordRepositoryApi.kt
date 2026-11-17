package ru.yeahub.authentication.impl.forgotpassword.domain.repository

interface ForgotPasswordRepositoryApi {
    suspend fun resetPassword(email: String): String
}