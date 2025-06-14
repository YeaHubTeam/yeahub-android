package ru.yeahub.network_api.models

data class GetSpecializationResponse(
    val id: Long,
    val title: String,
    val description: String,
    val imageSrc: String?,
    val createdAt: String,
    val updatedAt: String,
)
