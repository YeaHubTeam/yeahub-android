package ru.yeahub.network_api.models

data class GetCollectionsResponse(
    val page: Int?,
    val limit: Int?,
    val data: List<GetCollectionResponse>,
    val total: Int
)