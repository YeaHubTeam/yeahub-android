package ru.yeahub.network_api.models

data class GetFeatureFlagsResponse(
    val total: Int,
    val page: Int,
    val limit: Int,
    val data: List<GetFeatureFlagResponse>
)
