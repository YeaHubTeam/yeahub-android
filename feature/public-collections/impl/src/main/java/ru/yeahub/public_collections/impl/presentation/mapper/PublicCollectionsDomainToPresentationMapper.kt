package ru.yeahub.public_collections.impl.presentation.mapper

import ru.yeahub.public_collections.impl.domain.entity.PublicCollectionModel
import ru.yeahub.public_collections.impl.ui.PublicCollectionUiModel

class PublicCollectionsDomainToPresentationMapper {

    private fun mapCollectionModelToUiModel(
        publicCollectionModel: PublicCollectionModel
    ): PublicCollectionUiModel =
        PublicCollectionUiModel(
            id = publicCollectionModel.id,
            collectionTitle = publicCollectionModel.collectionTitle,
            descriptionText = publicCollectionModel.descriptionText,
            imageUrl = publicCollectionModel.imageUrl,
            questionsCount = publicCollectionModel.questionsCount
        )

    fun mapCollectionModelListToUiModelList(
        publicCollectionModelList: List<PublicCollectionModel>
    ): List<PublicCollectionUiModel> = publicCollectionModelList.map { mapCollectionModelToUiModel(it) }
}
