package ru.yeahub.public_collections.impl.data.repository

import ru.yeahub.network_api.NetworkProvider
import ru.yeahub.public_collections.impl.data.mapper.PublicCollectionDataToDomainMapper
import ru.yeahub.public_collections.impl.domain.entity.GetCollectionsResponseEntity
import ru.yeahub.public_collections.impl.domain.repository.PublicCollectionRepository
import ru.yeahub.public_collections.impl.presentation.viewmodel.PublicCollectionsRequest
import timber.log.Timber

private const val TAG = "Viva"
class PublicCollectionRepositoryImpl(
    private val networkProvider: NetworkProvider,
    private val publicCollectionDataToDomainMapper: PublicCollectionDataToDomainMapper
) : PublicCollectionRepository {
    override suspend fun getPublicCollection(request: PublicCollectionsRequest): GetCollectionsResponseEntity {
        val result = publicCollectionDataToDomainMapper.dataListToDomainList(
            networkProvider.apiService.getPublicCollections(
                page = request.page,
                limit = request.limit,
                specializationsId = request.specializationsId,
                isFree = request.isFree
            )
        )
        Timber.tag(TAG).d("Получаем данные в коллекции: $result")
        return result
    }
}
