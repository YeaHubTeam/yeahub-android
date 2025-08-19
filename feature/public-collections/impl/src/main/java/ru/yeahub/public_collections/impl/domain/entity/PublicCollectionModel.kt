package ru.yeahub.public_collections.impl.domain.entity

data class PublicCollectionModel(
    val id: Int,
    val collectionTitle: String,
    val descriptionText: String,
    val imageUrl: String,
    val questionsCount: Int,
)
