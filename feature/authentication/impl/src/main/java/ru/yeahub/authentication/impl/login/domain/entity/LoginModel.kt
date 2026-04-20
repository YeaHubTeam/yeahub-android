package ru.yeahub.authentication.impl.login.domain.entity

/**
 * Данные для авторизации
 */
data class LoginModel(
    val email: String,
    val password: String,
)