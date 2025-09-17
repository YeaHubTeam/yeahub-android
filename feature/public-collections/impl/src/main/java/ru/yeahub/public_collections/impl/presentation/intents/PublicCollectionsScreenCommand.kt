package ru.yeahub.public_collections.impl.presentation.intents

sealed interface PublicCollectionsScreenCommand {
    data object OnBackClick : PublicCollectionsScreenCommand
    data class OnQuestionsListClick(val collectionId: Int, val title: String) : PublicCollectionsScreenCommand
}