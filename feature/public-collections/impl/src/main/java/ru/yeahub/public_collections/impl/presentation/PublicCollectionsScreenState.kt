package ru.yeahub.public_collections.impl.presentation

sealed class PublicCollectionsScreenState() {

    data object Initial : PublicCollectionsScreenState()

    data object Loading : PublicCollectionsScreenState()

    data class Loaded(
        val collectionItemList: List<Item>,
        val isEndReached: Boolean,
        val isLoadingNextPage: Boolean,
    ) : PublicCollectionsScreenState() {
        data class Item(
            val id: Int,
            val collectionTitle: String,
            val descriptionText: String,
            val imageUrl: String,
            val questionsCount: Int,
        )
    }

    data class Error(
        val currentList: List<Loaded.Item>,
        val throwable: Throwable
    ) : PublicCollectionsScreenState()
}