package ru.yeahub.authentication.impl.registration.domain.entity

data class RegistrationModel(
    val nickname: String,
    val email: String,
    val password: String,
    val isMailingAccepted: Boolean,
)
