package ru.yeahub.public_collections.impl.presentation

import ru.yeahub.core_utils.common.TextOrResource

sealed interface PublicCollectionsScreenState {
    val header: TextOrResource

    data class Loading(override val header: TextOrResource) : PublicCollectionsScreenState

    data class Loaded(
        override val header: TextOrResource,
        val collectionItemList: List<Item>,
        val isEndReached: Boolean,
        val isLoadingNextPage: Boolean,
    ) : PublicCollectionsScreenState {
        data class Item(
            val id: Int,
            val collectionTitle: String,
            val descriptionText: String,
            val imageUrl: String,
            val questionsCount: Int,
        )
    }

    data class Error(
        override val header: TextOrResource,
        val currentList: List<Loaded.Item>,
        val throwable: Throwable
    ) : PublicCollectionsScreenState
}