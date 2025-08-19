package ru.yeahub.public_collections.impl.domain.entity

data class PublicCollectionsModel(
    val page: Int?,
    val limit: Int?,
    val data: List<PublicCollectionModel>,
    val total: Int
)
