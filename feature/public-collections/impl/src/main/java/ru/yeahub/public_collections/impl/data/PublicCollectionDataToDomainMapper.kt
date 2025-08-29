package ru.yeahub.public_collections.impl.data

import ru.yeahub.network_api.models.GetCollectionResponse
import ru.yeahub.network_api.models.GetCollectionsResponse
import ru.yeahub.public_collections.impl.domain.GetCollectionResponseEntity
import ru.yeahub.public_collections.impl.domain.GetCollectionsResponseEntity

class PublicCollectionDataToDomainMapper {

    fun dataListToDomainList(
        dataResponse: GetCollectionsResponse
    ): GetCollectionsResponseEntity =
        GetCollectionsResponseEntity(
            page = dataResponse.page ?: 1,
            limit = dataResponse.limit ?: 10,
            data = dataResponse.data.map { dataItemToDomainItem(it) },
            total = dataResponse.total
        )

    private fun dataItemToDomainItem(
        dataItem: GetCollectionResponse
    ): GetCollectionResponseEntity =
        GetCollectionResponseEntity(
            id = dataItem.id,
            title = dataItem.title,
            description = dataItem.description,
            imageSrc = dataItem.imageSrc ?: "",
            questionsCount = dataItem.questionsCount
        )
}