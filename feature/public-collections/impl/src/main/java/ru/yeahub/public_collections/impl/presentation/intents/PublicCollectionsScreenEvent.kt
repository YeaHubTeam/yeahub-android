package ru.yeahub.public_collections.impl.presentation.intents

sealed interface PublicCollectionsScreenEvent {
    data object TODO : PublicCollectionsScreenEvent
}