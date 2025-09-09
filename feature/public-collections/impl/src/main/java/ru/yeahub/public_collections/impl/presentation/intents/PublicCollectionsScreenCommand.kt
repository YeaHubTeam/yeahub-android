package ru.yeahub.public_collections.impl.presentation.intents

sealed interface PublicCollectionsScreenCommand {
    data object TODO : PublicCollectionsScreenCommand
}