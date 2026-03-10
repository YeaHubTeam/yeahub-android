package ru.yeahub.impl.domain

interface ForgotPasswordRepository {

    suspend fun sendResetLink(email: String): ForgotPasswordResult
}