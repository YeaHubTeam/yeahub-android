package ru.yeahub.public_collections.impl.ui

sealed class PublicCollectionsScreenState() {

    data object Initial : PublicCollectionsScreenState()

    data object Loading : PublicCollectionsScreenState()

    data class Loaded(
        val collectionItem: List<PublicCollectionUiModel>,
        val isEndReached: Boolean,
        val isLoadingNextPage: Boolean
    ) : PublicCollectionsScreenState()

    data class Error(
        val questions: List<PublicCollectionUiModel>,
        val throwable: Throwable
    ) : PublicCollectionsScreenState()
}