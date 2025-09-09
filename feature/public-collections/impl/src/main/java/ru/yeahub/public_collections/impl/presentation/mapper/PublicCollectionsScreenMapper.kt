package ru.yeahub.public_collections.impl.presentation.mapper

import ru.yeahub.core_utils.common.TextOrResource

class PublicCollectionsScreenMapper {
    fun getScreenState(): PublicCollectionsScreenState =
        PublicCollectionsScreenState.Loaded(
            header = TextOrResource.Text("React"),
            collectionItemList = listOf(
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
                    id = 4,
                    collectionTitle = "Собеседование Middle WB",
                    descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
                    imageUrl = "",
                    questionsCount = 54
                ),
                PublicCollectionsScreenState.Loaded.Item(
                    id = 5,
                    collectionTitle = "Собеседование Middle Яндекс",
                    descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
                    imageUrl = "",
                    questionsCount = 154
                ),
                PublicCollectionsScreenState.Loaded.Item(
                    id = 6,
                    collectionTitle = "Собеседование Middle ВТБ",
                    descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
                    imageUrl = "",
                    questionsCount = 154
                )
            ),
            isEndReached = false,
            isLoadingNextPage = false
        )
}
