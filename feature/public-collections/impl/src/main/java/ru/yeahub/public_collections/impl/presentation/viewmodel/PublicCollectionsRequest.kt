package ru.yeahub.public_collections.impl.presentation.viewmodel

data class PublicCollectionsRequest(
    val page: Int,
    val limit: Int,
    val specializationsId: Long,
    val isFree: Boolean = true
)
