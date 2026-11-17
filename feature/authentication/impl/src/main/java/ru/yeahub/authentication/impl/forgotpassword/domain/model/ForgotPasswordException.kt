package ru.yeahub.authentication.impl.forgotpassword.domain.model

class ForgotPasswordException(
    val httpCode: Int? = null,
    val serverMessage: String? = null,
    cause: Throwable? = null
) : Exception(serverMessage ?: cause?.message, cause)