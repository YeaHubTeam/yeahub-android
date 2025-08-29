package ru.yeahub.public_collections.impl.domain

data class GetCollectionResponseEntity(
    val id: Int,
    val title: String,
    val description: String,
    val imageSrc: String?,
    val questionsCount: Int,
)