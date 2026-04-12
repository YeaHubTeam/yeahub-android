package ru.yeahub.network_api.models

data class UpdatePublicUserResponse(
    val id: String,
    val username: String?,
    val email: String?,
    val avatarUrl: String?,
    val city: String?,
)
