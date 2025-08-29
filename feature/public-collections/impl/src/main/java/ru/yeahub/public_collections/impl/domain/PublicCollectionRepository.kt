package ru.yeahub.public_collections.impl.domain

interface PublicCollectionRepository {

    suspend fun getPublicCollection(
        page: Int,
        limit: Int,
        specializationEntity: NestedSpecializationEntity?//тут спросить
    ): GetCollectionsResponseEntity
}