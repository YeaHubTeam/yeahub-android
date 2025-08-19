package ru.yeahub.public_collections.impl.ui

data class PublicCollectionUiModel(
    val id: Int,
    val collectionTitle: String,
    val descriptionText: String,
    val imageUrl: String,
    val questionsCount: Int,
)
