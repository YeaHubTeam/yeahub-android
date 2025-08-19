package ru.yeahub.public_collections.impl.ui

sealed class PublicCollectionsResult {

    data object NavigateBack : PublicCollectionsResult()

    data class NavigateToDetail(val id: String) : PublicCollectionsResult()
}