package ru.yeahub.public_collections.impl.domain

data class PublicCollectionsRequest(
    val page: Int,
    val limit: Int,
    val specializationsId: Long
)
