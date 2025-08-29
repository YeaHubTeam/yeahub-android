package ru.yeahub.public_collections.impl.data

import ru.yeahub.public_collections.impl.domain.GetCollectionsResponseEntity
import ru.yeahub.public_collections.impl.domain.NestedSpecializationEntity
import ru.yeahub.public_collections.impl.domain.PublicCollectionRepository

class PublicCollectionRepositoryImpl(
    private val apiService: ApiService
): PublicCollectionRepository {
    override suspend fun getPublicCollection(
        page: Int,
        limit: Int,
        specializationEntity: NestedSpecializationEntity?
    ): GetCollectionsResponseEntity {
       val data = apiService.getPublicCollections(
            page = page,
           limit = limit
        )
    }
}