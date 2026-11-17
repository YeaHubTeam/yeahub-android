package ru.yeahub.network_api.models

data class ForgotPasswordErrorDto(
    val message: String?,
    val statusCode: Int?,
    val description: String?
)