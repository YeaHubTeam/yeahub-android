package ru.yeahub.network_api.models

data class RegistrationRequestDto(
    val nickname: String,
    val email: String,
    val password: String,
    val isMailingAccepted: Boolean,
)
