package ru.yeahub.public_collections.impl.presentation.intents

sealed interface PublicCollectionsScreenResult {
    data object NavigateBack : PublicCollectionsScreenResult
    data class NavigateToQuestions(val collectionId: Int) : PublicCollectionsScreenResult
}