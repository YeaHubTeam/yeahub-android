package ru.yeahub.public_collections.impl.domain

data class GetCollectionsResponseEntity(
    val page: Int?,
    val limit: Int?,
    val data: List<GetCollectionResponseEntity>,
    val total: Int
)