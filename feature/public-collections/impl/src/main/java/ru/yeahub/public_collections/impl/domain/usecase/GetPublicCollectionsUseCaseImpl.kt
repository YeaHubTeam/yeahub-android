package ru.yeahub.public_collections.impl.domain.usecase

import ru.yeahub.public_collections.impl.domain.entity.GetCollectionsResponseEntity
import ru.yeahub.public_collections.impl.domain.repository.PublicCollectionRepository
import ru.yeahub.public_collections.impl.presentation.viewmodel.PublicCollectionsRequest

class GetPublicCollectionsUseCaseImpl(private val publicCollectionRepository: PublicCollectionRepository) : GetPublicCollectionsUseCase {

    override suspend fun invoke(request: PublicCollectionsRequest): GetCollectionsResponseEntity {
        return publicCollectionRepository.getPublicCollection(request)
    }
}