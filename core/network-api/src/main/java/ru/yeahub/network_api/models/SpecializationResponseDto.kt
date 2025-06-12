package ru.yeahub.network_api.models

data class SpecializationResponseDto(
    val page: Int?,
    val limit: Int?,
    val total: Int?,
    val data: List<SpecializationDto>?
)
