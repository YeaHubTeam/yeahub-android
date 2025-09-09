package ru.yeahub.public_collections.impl.presentation.mapper

import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.pagerImpl.YeaHubPagerState
import ru.yeahub.public_collections.impl.domain.entity.GetCollectionResponseEntity
import ru.yeahub.public_collections.impl.presentation.PublicCollectionsScreenState

class PublicCollectionsScreenMapper {


    fun mapPagerStateToScreenState(
        pagerState: YeaHubPagerState<GetCollectionResponseEntity>,
        header: String
    ): PublicCollectionsScreenState {
        return when (pagerState) {
            is YeaHubPagerState.Initial -> PublicCollectionsScreenState.Initial(
                header = TextOrResource.Text(header)
            )

            is YeaHubPagerState.Loading -> {
                if (pagerState.items.isEmpty()) {
                    PublicCollectionsScreenState.Loading(
                        header = TextOrResource.Text(header)
                    )
                } else {
                    PublicCollectionsScreenState.Loaded(
                        collectionPublicCollectionVOList = mapCollectionResponseEntityListToResponseVOList(
                            pagerState.items
                        ),
                        isEndReached = false,
                        isLoadingNextPage = true,
                        header = TextOrResource.Text(header)
                    )
                }
            }

            is YeaHubPagerState.Loaded -> {
                PublicCollectionsScreenState.Loaded(
                    collectionPublicCollectionVOList = mapCollectionResponseEntityListToResponseVOList(
                        pagerState.items
                    ),
                    isEndReached = pagerState.isEndReached,
                    isLoadingNextPage = false,
                    header = TextOrResource.Text(header)
                )
            }

            is YeaHubPagerState.Error -> {
                PublicCollectionsScreenState.Error(
                    currentList = mapCollectionResponseEntityListToResponseVOList(
                        pagerState.items
                    ),
                    throwable = pagerState.throwable,
                    header = TextOrResource.Text(header)
                )
            }
        }
    }

    private fun mapCollectionResponseEntityToResponseVO(
        publicGetCollectionResponseEntity: GetCollectionResponseEntity
    ): PublicCollectionsScreenState.Loaded.PublicCollectionVO {
        return PublicCollectionsScreenState.Loaded.PublicCollectionVO(
            id = publicGetCollectionResponseEntity.id,
            collectionTitle = publicGetCollectionResponseEntity.title,
            descriptionText = publicGetCollectionResponseEntity.description,
            imageUrl = publicGetCollectionResponseEntity.imageSrc,
            questionsCount = publicGetCollectionResponseEntity.questionsCount
        )
    }

    private fun mapCollectionResponseEntityListToResponseVOList(
        publicGetCollectionResponseEntity: List<GetCollectionResponseEntity>
    ): List<PublicCollectionsScreenState.Loaded.PublicCollectionVO> {
        return publicGetCollectionResponseEntity.map { entity ->
            mapCollectionResponseEntityToResponseVO(entity)
        }
    }
}
