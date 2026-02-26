package ru.yeahub.network_api.models

data class RegistrationRequest(
    val nickname: String,
    val email: String,
    val password: String,
    val isMailingAccepted: Boolean,
)
