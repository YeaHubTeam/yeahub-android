package ru.yeahub.public_collections.impl.presentation

import ru.yeahub.core_utils.common.TextOrResource

sealed interface PublicCollectionsScreenState {
    val header: TextOrResource

    data class Initial(override val header: TextOrResource) : PublicCollectionsScreenState

    data class Loading(override val header: TextOrResource) : PublicCollectionsScreenState

    data class Loaded(
        override val header: TextOrResource,
        val collectionPublicCollectionVOList: List<PublicCollectionVO>,
        val isEndReached: Boolean,
        val isLoadingNextPage: Boolean,
    ) : PublicCollectionsScreenState {
        data class PublicCollectionVO(
            val id: Int,
            val collectionTitle: String,
            val descriptionText: String,
            val imageUrl: String?,
            val questionsCount: Int,
        )
    }

    data class Error(
        override val header: TextOrResource,
        val currentList: List<Loaded.PublicCollectionVO>,
        val throwable: Throwable
    ) : PublicCollectionsScreenState
    data class Empty(override val header: TextOrResource) : PublicCollectionsScreenState
}