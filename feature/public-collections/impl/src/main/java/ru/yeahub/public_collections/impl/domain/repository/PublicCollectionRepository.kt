package ru.yeahub.public_collections.impl.domain.repository

import ru.yeahub.public_collections.impl.domain.entity.GetCollectionsResponseEntity
import ru.yeahub.public_collections.impl.presentation.viewmodel.PublicCollectionsRequest

interface PublicCollectionRepository {

    suspend fun getPublicCollection(
        request: PublicCollectionsRequest
    ): GetCollectionsResponseEntity
}