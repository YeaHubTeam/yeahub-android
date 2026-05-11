package ru.yeahub.network_api.models

data class GetFeatureFlagResponse(
    val id: String,
    val flag: String,
    val enabled: Boolean,
    val description: String?,
    val roles: List<String>,
    val clientType: String,
    val createdAt: String,
    val updatedAt: String
)
