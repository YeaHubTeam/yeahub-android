package ru.yeahub.public_collections.impl.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import ru.yeahub.core_ui.component.CollectionCard
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.public_collections.impl.presentation.PublicCollectionsScreenState

@Composable
fun PublicCollectionItem(
    collectionItem: PublicCollectionsScreenState.Loaded.Item,
    onClickItem: (itemId: String) -> Unit
) {
    CollectionCard(
        collectionTitle = collectionItem.collectionTitle,
        descriptionText = collectionItem.descriptionText,
        imageUrl = collectionItem.imageUrl,
        questionsCount = collectionItem.questionsCount,
        onCollectionClick = { onClickItem(collectionItem.id.toString()) }
    )
}

@StaticPreview
@Composable
fun PublicCollectionPreview() {
    val mockCollections = listOf(
        PublicCollectionsScreenState.Loaded.Item(
            id = 1,
            collectionTitle = "Собеседование Middle Сбер",
            descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
            imageUrl = "",
            questionsCount = 35
        ),
        PublicCollectionsScreenState.Loaded.Item(
            id = 2,
            collectionTitle = "Собеседование Middle Т-банк",
            descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
            imageUrl = "",
            questionsCount = 50
        ),
        PublicCollectionsScreenState.Loaded.Item(
            id = 3,
            collectionTitle = "Собеседование Middle Ozon",
            descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
            imageUrl = "",
            questionsCount = 24
        ),
        PublicCollectionsScreenState.Loaded.Item(
            id = 3,
            collectionTitle = "Собеседование Middle WB",
            descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
            imageUrl = "",
            questionsCount = 24
        )
    )
    LazyColumn {
        items(mockCollections) { collectionItem ->
            PublicCollectionItem(
                collectionItem = collectionItem,
                onClickItem = { itemId -> }
            )
        }
    }
}