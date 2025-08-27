package ru.yeahub.public_collections.impl.presentation

sealed interface PublicCollectionsScreenEvent {
    data object TODO : PublicCollectionsScreenEvent
}