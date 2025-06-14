package ru.yeahub.network_api.models

data class GetSpecializationsResponse(
    val page: Long?,
    val limit: Long?,
    val data: List<NestedSpecializationResponse>,
    val total: Long
)
