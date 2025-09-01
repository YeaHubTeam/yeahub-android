package ru.yeahub.public_collections.impl.domain

interface PublicCollectionRepository {

    suspend fun getPublicCollection(
        request: PublicCollectionsRequest
    ): GetCollectionsResponseEntity
}