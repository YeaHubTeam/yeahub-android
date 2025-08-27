package ru.yeahub.public_collections.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.yeahub.core_ui.component.CollectionCard
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.example.dynamicPreview.ProvidePreviewCompositionLocals
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.Theme.colors
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.common.observe
import ru.yeahub.public_collections.impl.R
import ru.yeahub.public_collections.impl.presentation.PublicCollectionsScreenCommand
import ru.yeahub.public_collections.impl.presentation.PublicCollectionsScreenEvent
import ru.yeahub.public_collections.impl.presentation.PublicCollectionsScreenMapper
import ru.yeahub.public_collections.impl.presentation.PublicCollectionsScreenState
import ru.yeahub.public_collections.impl.presentation.PublicCollectionsViewModel

@Composable
fun PublicCollectionsScreen(
    viewModel: PublicCollectionsViewModel
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    ScreenUI(
        state = state,
        onEvent = viewModel::onEvent
    )

    HandleCommand(
        commandFlow = viewModel.commands
    )
}

@Composable
fun ScreenUI(
    state: PublicCollectionsScreenState,
    onEvent: (PublicCollectionsScreenEvent) -> Unit
) {
    Scaffold(
        containerColor = colors.black10,
        topBar = {
            TopAppBarWithBottomBorder(
                title = state.header.getString(LocalContext.current),
                onBackClick = {}
            )
        }
    ) { innerPadding ->

        when (state) {
            is PublicCollectionsScreenState.Error -> ErrorScreen(
                error = state.throwable.message ?: "",
                titleText = "УПС!",
                backText = "Назад",
                unknownErrorText = "Не удалось загрузить данные",
                onBack = {}
            )

            is PublicCollectionsScreenState.Loaded -> BasePublicCollectionScreen(
                modifier = Modifier.padding(innerPadding),
                state = state,
                onClickItem = {}
            )

            is PublicCollectionsScreenState.Loading -> PublicCollectionLoading(
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
fun HandleCommand(
    commandFlow: Flow<PublicCollectionsScreenCommand>,
) {
    commandFlow.observe { command ->
        when (command) {
            PublicCollectionsScreenCommand.TODO -> {
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithBottomBorder(
    title: String,
    borderColor: Color = colors.black50,
    borderThickness: Dp = 1.dp,
    onBackClick: () -> Unit
) {
    val density = LocalDensity.current
    val borderThicknessPx = with(density) { borderThickness.toPx() }
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = Theme.typography.body3Accent,
                color = colors.black900
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = colors.purple700
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colors.white900
        ),
        modifier = Modifier
            .fillMaxWidth()
            .drawWithContent {
                drawContent()
                val y = size.height - borderThicknessPx / 2f
                drawLine(
                    color = borderColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = borderThicknessPx
                )
            }
    )
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
    ) {
        Text(
            modifier = Modifier
                .padding(start = 12.dp, top = 16.dp),
            text = stringResource(R.string.collections),
            style = Theme.typography.body5Strong,
            color = colors.black900
        )

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState,
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

class ListPublicCollectionScreenProvider : PreviewParameterProvider<ScreenParams> {
    override val values: Sequence<ScreenParams> = sequenceOf(
        ScreenParams(
            state = PublicCollectionsScreenState.Loaded(
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
                    ),
                ),
                isEndReached = false,
                isLoadingNextPage = false,
                header = TextOrResource.Text(text = "React")
            ),
        ),
        ScreenParams(
            state = PublicCollectionsScreenState.Error(
                currentList = listOf(),
                throwable = Throwable("Не удалось загрузить данные"),
                header = TextOrResource.Text(text = "React")
            ),
        ),
        ScreenParams(
            state = PublicCollectionsScreenState.Loading(header = TextOrResource.Text(text = "React"))
        )
    )
}

data class ScreenParams(
    val state: PublicCollectionsScreenState,
)

@StaticPreview
@Composable
fun ShowScreenPreview(
    @PreviewParameter(ListPublicCollectionScreenProvider::class) params: ScreenParams
) {
    ScreenUI(state = params.state, onEvent = {})
}

@Preview
@Composable
fun PublicCollectionsScreenDynamicPreview() {
    ProvidePreviewCompositionLocals {
        PublicCollectionsScreen(viewModel = PublicCollectionsViewModel(PublicCollectionsScreenMapper()))
    }
}
