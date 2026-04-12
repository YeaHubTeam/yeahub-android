package ru.yeahub.network_api.models

data class UpdateUserRequest(
    val username: String?,
    val country: String?,
    val city: String?,
    val birthday: String?,
    val address: String?,
    val avatarUrl: String?,
    val avatarImage: String?,
)
