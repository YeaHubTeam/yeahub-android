package ru.yeahub.public_collections.impl.domain.usecase

import ru.yeahub.public_collections.impl.domain.entity.GetCollectionsResponseEntity
import ru.yeahub.public_collections.impl.presentation.viewmodel.PublicCollectionsRequest

interface GetPublicCollectionsUseCase {

    suspend operator fun invoke(request: PublicCollectionsRequest) : GetCollectionsResponseEntity
}