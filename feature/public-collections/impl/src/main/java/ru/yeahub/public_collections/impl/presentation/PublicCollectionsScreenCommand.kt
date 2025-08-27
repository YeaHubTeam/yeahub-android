package ru.yeahub.public_collections.impl.presentation

sealed interface PublicCollectionsScreenCommand {
    data object TODO: PublicCollectionsScreenCommand
}