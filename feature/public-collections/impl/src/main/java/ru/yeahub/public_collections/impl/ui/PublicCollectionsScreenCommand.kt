package ru.yeahub.public_collections.impl.ui

sealed class PublicCollectionsScreenCommand {

    data object OnBackClick : PublicCollectionsScreenCommand()
    data class OpenDetailScreen(val id: String) : PublicCollectionsScreenCommand()
}