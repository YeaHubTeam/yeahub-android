package ru.yeahub.public_collections.impl.ui.views

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import ru.yeahub.core_ui.component.CollectionCard
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.public_collections.impl.ui.PublicCollectionUiModel

@Composable
fun PublicCollectionItem(
    collectionItem: PublicCollectionUiModel,
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
        PublicCollectionUiModel(
            id = 1,
            collectionTitle = "Собеседование Middle Сбер",
            descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
            imageUrl = "",
            questionsCount = 35
        ),
        PublicCollectionUiModel(
            id = 2,
            collectionTitle = "Собеседование Middle Т-банк",
            descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
            imageUrl = "",
            questionsCount = 50
        ),
        PublicCollectionUiModel(
            id = 3,
            collectionTitle = "Собеседование Middle Ozon",
            descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
            imageUrl = "",
            questionsCount = 24
        ),
        PublicCollectionUiModel(
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