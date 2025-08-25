package ru.yeahub.public_collections.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.yeahub.core_ui.component.CollectionCard
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.Theme.colors
import ru.yeahub.public_collections.impl.presentation.PublicCollectionsScreenState
import ru.yeahub.public_collections.impl.presentation.PublicCollectionsViewModel

val FIGMA_HORIZONTAL_PADDING = 16.dp
val FIGMA_VERTICAL_TITLE_PADDING = 22.dp
val FIGMA_VERTICAL_CARD_PADDING = 16.dp //need edge_to_edge = 16

@Composable
fun PublicCollectionScreen(
    viewModel: PublicCollectionsViewModel,
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    when (state) {
        is PublicCollectionsScreenState.Error -> ErrorScreen(
            error = (state as PublicCollectionsScreenState.Error).throwable.message ?: "",
            titleText = "УПС!",
            backText = "Назад",
            unknownErrorText = "Не удалось загрузить данные",
            onBack = {}
        )

        PublicCollectionsScreenState.Initial -> TODO()

        is PublicCollectionsScreenState.Loaded -> BasePublicCollectionScreen(
            state = state as PublicCollectionsScreenState.Loaded,
            onClickItem = {}
        )

        PublicCollectionsScreenState.Loading -> PublicCollectionLoading()
    }
}

@Composable
fun BasePublicCollectionScreen(
    modifier: Modifier = Modifier,
    state: PublicCollectionsScreenState.Loaded,
    onClickItem: (itemId: String) -> Unit
) {
    val lazyListState = rememberLazyListState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.black10),
    ) {
        Text(
            modifier = modifier.padding(
                vertical = FIGMA_VERTICAL_TITLE_PADDING,
                horizontal = FIGMA_HORIZONTAL_PADDING
            ),
            text = state.specializationFilter,
            style = Theme.typography.body3Accent,
            color = colors.black900
        )


        Text(
            modifier = modifier.padding(
                vertical = FIGMA_VERTICAL_TITLE_PADDING,
                horizontal = FIGMA_HORIZONTAL_PADDING
            ),
            text = "Коллекции",
            style = Theme.typography.body5Strong,
            color = colors.black900
        )

        LazyColumn(
            modifier = modifier.padding(horizontal = FIGMA_HORIZONTAL_PADDING),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(FIGMA_VERTICAL_CARD_PADDING)
        ) {
            items(
                items = state.collectionItemList,
                key = { it.id }
            ) { collectionItem ->
                CollectionCard(
                    collectionTitle = collectionItem.collectionTitle,
                    descriptionText = collectionItem.descriptionText,
                    imageUrl = collectionItem.imageUrl,
                    questionsCount = collectionItem.questionsCount,
                    onCollectionClick = { onClickItem }
                )
            }
        }
    }
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
        ),
    )
    val state = PublicCollectionsScreenState.Loaded(
        collectionItemList = mockCollections,
        isEndReached = false,
        isLoadingNextPage = false,
        specializationFilter = "React"
    )
    BasePublicCollectionScreen(
        state = state,
        onClickItem = {}
    )
}

@StaticPreview
@Composable
fun PublicCollectionPreviewError() {
    val state = PublicCollectionsScreenState.Error(listOf(), throwable = Throwable("Не удалось загрузить данные"))
    ErrorScreen(
        error = state.throwable.message ?: "",
        titleText = "УПС!",
        backText = "Назад",
        unknownErrorText = "Не удалось загрузить данные",
        onBack = {}
    )
}


@StaticPreview
@Composable
fun PublicCollectionPreviewLoading() {
    val state = PublicCollectionsScreenState.Loading
    PublicCollectionLoading()
}