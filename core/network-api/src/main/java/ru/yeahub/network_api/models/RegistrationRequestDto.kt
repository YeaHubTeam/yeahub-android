package ru.yeahub.network_api.models

data class RegistrationRequestDto(
    val username: String,
    val email: String,
    val password: String,
)
