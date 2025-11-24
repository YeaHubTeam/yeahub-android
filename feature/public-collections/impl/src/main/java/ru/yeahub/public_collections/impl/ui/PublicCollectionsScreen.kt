package ru.yeahub.public_collections.impl.ui

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.yeahub.core_ui.component.CollectionCard
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.example.dynamicPreview.ProvidePreviewCompositionLocals
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.Theme.colors
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.public_collections.impl.R
import ru.yeahub.public_collections.impl.domain.entity.GetCollectionResponseEntity
import ru.yeahub.public_collections.impl.domain.entity.GetCollectionsResponseEntity
import ru.yeahub.public_collections.impl.domain.usecase.GetPublicCollectionsUseCase
import ru.yeahub.public_collections.impl.presentation.PublicCollectionsScreenState
import ru.yeahub.public_collections.impl.presentation.intents.PublicCollectionsScreenCommand
import ru.yeahub.public_collections.impl.presentation.intents.PublicCollectionsScreenEvent
import ru.yeahub.public_collections.impl.presentation.intents.PublicCollectionsScreenResult
import ru.yeahub.public_collections.impl.presentation.mapper.PublicCollectionsScreenMapper
import ru.yeahub.public_collections.impl.presentation.viewmodel.PublicCollectionsRequest
import ru.yeahub.public_collections.impl.presentation.viewmodel.PublicCollectionsViewModel
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

@Composable
fun PublicCollectionsScreen(
    onResult: (PublicCollectionsScreenResult) -> Unit,
    specializationId: Long,
    header: String
) {
    val viewModel: PublicCollectionsViewModel = koinViewModel(
        parameters = { parametersOf(specializationId, header) }
    )

    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val lazyListState: LazyListState = rememberLazyListState()

    LaunchedEffect(lazyListState, state) {
        snapshotFlow { lazyListState.layoutInfo }
            .map { layout ->
                val lastItem = layout.visibleItemsInfo.lastOrNull()
                val totalItems = layout.totalItemsCount
                val isAlmostAtEnd =
                    lastItem != null && totalItems > 0 && lastItem.index >= totalItems - RESPONSE_THRESHOLD
                isAlmostAtEnd
            }.filter { shouldTriggerLoad -> shouldTriggerLoad }
            .collect {
                val canLoadMore = when (val currentScreenState = viewModel.screenState.value) {
                    is PublicCollectionsScreenState.Loaded -> {
                        !currentScreenState.isEndReached && !currentScreenState.isLoadingNextPage
                    }

                    else -> false
                }
                if (canLoadMore) {
                    viewModel.onEvent(PublicCollectionsScreenEvent.LoadNextPage)
                }
            }
    }

    ScreenUI(
        listState = lazyListState,
        state = state,
        onEvent = viewModel::onEvent
    )

    HandleCommand(
        commandFlow = viewModel.commands,
        onResult = onResult
    )
}

@Composable
fun ScreenUI(
    listState: LazyListState,
    state: PublicCollectionsScreenState,
    onEvent: (PublicCollectionsScreenEvent) -> Unit
) {
    Scaffold(
        containerColor = colors.black10,
        topBar = {
            TopAppBarWithBottomBorder(
                title = state.header.getString(LocalContext.current),
                onBackClick = { onEvent(PublicCollectionsScreenEvent.OnBackClick) }
            )
        }
    ) { innerPadding ->

        when (state) {
            is PublicCollectionsScreenState.Initial, is PublicCollectionsScreenState.Loading -> {
                PublicCollectionLoading(
                    modifier = Modifier.padding(innerPadding),
                    listState = listState,
                )
            }

            is PublicCollectionsScreenState.Loaded -> {
                if (state.collectionPublicCollectionVOList
                        .isEmpty() && !state.isLoadingNextPage && state.isEndReached
                ) {
                    PublicCollectionLoading(
                        modifier = Modifier.padding(innerPadding),
                        listState = listState,
                    )
                } else {
                    CollectionsListWithLoadMore(
                        modifier = Modifier.padding(innerPadding),
                        listState = listState,
                        collections = state.collectionPublicCollectionVOList,
                        isLoadingNextPage = state.isLoadingNextPage,
                        paginationError = null,
                        onRetryPagination = { onEvent(PublicCollectionsScreenEvent.Refresh) },
                        onClickItem = { id, title ->
                            onEvent(
                                PublicCollectionsScreenEvent.OnQuestionsListClick(
                                    id,
                                    title
                                )
                            )
                        }
                    )
                }
            }

            is PublicCollectionsScreenState.Error -> {
                if (state.currentList.isEmpty()) {
                    ErrorScreen(
                        error = state.throwable.message ?: "",
                        titleText = TextOrResource.Resource(R.string.title_error_screen_text),
                        backText = TextOrResource.Resource(R.string.back_error_screen_text),
                        unknownErrorText = TextOrResource.Resource(R.string.unknown_error_screen_text),
                        onBack = { onEvent(PublicCollectionsScreenEvent.OnBackClick) },
                        errorText = TextOrResource.Resource(R.string.error_screen_text)
                    )
                } else {
                    CollectionsListWithLoadMore(
                        modifier = Modifier.padding(innerPadding),
                        listState = listState,
                        collections = state.currentList,
                        isLoadingNextPage = false,
                        paginationError = state.throwable,
                        onRetryPagination = { onEvent(PublicCollectionsScreenEvent.Refresh) },
                        onClickItem = { id, title ->
                            onEvent(
                                PublicCollectionsScreenEvent.OnQuestionsListClick(
                                    id,
                                    title
                                )
                            )
                        }
                    )
                }
            }

            is PublicCollectionsScreenState.Empty -> {
                Empty()
            }
        }
    }
}

@Composable
private fun Empty(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.there_is_nothing),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CollectionsListWithLoadMore(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    collections: List<PublicCollectionsScreenState.Loaded.PublicCollectionVO>,
    isLoadingNextPage: Boolean,
    paginationError: Throwable?,
    onClickItem: (id: Int, title: String) -> Unit,
    onRetryPagination: () -> Unit,
) {
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
            state = listState,
        ) {
            if (collections.isNotEmpty()) {
                items(
                    items = collections,
                    key = { collections -> collections.id }
                ) { collection ->
                    CollectionCard(
                        collectionTitle = collection.collectionTitle,
                        descriptionText = collection.descriptionText,
                        imageUrl = collection.imageUrl,
                        questionsCount = collection.questionsCount,
                        onCollectionClick = {
                            onClickItem(
                                collection.id,
                                collection.collectionTitle
                            )
                        }
                    )
                }
            }
            item {
                LoadMoreHandler(
                    isLoading = isLoadingNextPage,
                    error = paginationError,
                    onRetry = onRetryPagination
                )
            }
        }
    }
}

@Composable
fun LoadMoreHandler(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    error: Throwable?,
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            error != null -> {
                val errorMessage =
                    getReadableErrorMessage(context = context, throwable = error)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Button(onClick = onRetry) {
                        Text(stringResource(id = R.string.retry_text))
                    }
                }
            }

            else -> {
                Spacer(modifier = Modifier.height(0.dp))
            }
        }
    }
}

@Composable
fun getReadableErrorMessage(context: Context, throwable: Throwable): String {
    return when (throwable) {
        is UnknownHostException -> {
            context.getString(R.string.no_internet_text)
        }

        is TimeoutException -> {
            context.getString(
                R.string.no_response_text
            )
        }

        else -> throwable.localizedMessage ?: context.getString(R.string.error_text)
    }
}

@Composable
fun HandleCommand(
    commandFlow: Flow<PublicCollectionsScreenCommand>,
    onResult: (PublicCollectionsScreenResult) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        commandFlow
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { command ->
                when (command) {
                    PublicCollectionsScreenCommand.OnBackClick -> onResult(
                        PublicCollectionsScreenResult.NavigateBack
                    )

                    is PublicCollectionsScreenCommand.OnQuestionsListClick -> onResult(
                        PublicCollectionsScreenResult.NavigateToQuestions(
                            command.collectionId,
                            command.title
                        )
                    )
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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        CenterAlignedTopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = Theme.typography.body3Accent,
                        color = colors.black900
                    )
                }
            },
            navigationIcon = {
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
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
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colors.white900
            ),
            windowInsets = WindowInsets(0, 0, 0, 0),
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = ru.yeahub.ui.R.dimen.app_bar_height_landscape))
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
    } else {
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
}

class ListPublicCollectionScreenProvider : PreviewParameterProvider<ScreenParams> {
    override val values: Sequence<ScreenParams> = sequenceOf(
        ScreenParams(
            state = PublicCollectionsScreenState.Loaded(
                collectionPublicCollectionVOList = listOf(
                    PublicCollectionsScreenState.Loaded.PublicCollectionVO(
                        id = 1,
                        collectionTitle = "Собеседование Middle Сбер",
                        descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
                        imageUrl = "",
                        questionsCount = 35
                    ),
                    PublicCollectionsScreenState.Loaded.PublicCollectionVO(
                        id = 2,
                        collectionTitle = "Собеседование Middle Т-банк",
                        descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
                        imageUrl = "",
                        questionsCount = 50
                    ),
                    PublicCollectionsScreenState.Loaded.PublicCollectionVO(
                        id = 3,
                        collectionTitle = "Собеседование Middle Ozon",
                        descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
                        imageUrl = "",
                        questionsCount = 24
                    ),
                    PublicCollectionsScreenState.Loaded.PublicCollectionVO(
                        id = 4,
                        collectionTitle = "Собеседование Middle WB",
                        descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
                        imageUrl = "",
                        questionsCount = 54
                    ),
                    PublicCollectionsScreenState.Loaded.PublicCollectionVO(
                        id = 5,
                        collectionTitle = "Собеседование Middle Яндекс",
                        descriptionText = "Техническое собеседование для экспертов по основным вопросам React",
                        imageUrl = "",
                        questionsCount = 154
                    ),
                    PublicCollectionsScreenState.Loaded.PublicCollectionVO(
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
    ScreenUI(
        state = params.state,
        onEvent = {},
        listState = rememberLazyListState(),
    )
}

@Preview(showBackground = true)
@Composable
fun PublicCollectionsScreenDynamicPreview() {
    val lazyListState = rememberLazyListState()

    val specializationId = 1L
    val header = "React"
    val mockMapper = remember { PublicCollectionsScreenMapper() }

    val mockGetPublicCollectionsByIUseCase = object : GetPublicCollectionsUseCase {
        override suspend fun invoke(request: PublicCollectionsRequest): GetCollectionsResponseEntity {
            delay(RESPONSE_DELAY)
            return GetCollectionsResponseEntity(
                page = request.page,
                limit = request.limit,
                data = (1..request.limit).map {
                    GetCollectionResponseEntity(
                        id = ((request.page - 1) * request.limit + it),
                        title = "Собеседование #${(request.page - 1) * request.limit + it}",
                        description = "Описание собеседования для React-разработчика",
                        imageSrc = "",
                        questionsCount = (20..100).random()
                    )
                },
                total = 50
            )
        }
    }

    val mockViewModel: PublicCollectionsViewModel = viewModelCreator {
        PublicCollectionsViewModel(
            mockMapper,
            mockGetPublicCollectionsByIUseCase,
            specializationId,
            header
        )
    }

    val state by mockViewModel.screenState.collectAsState()

    LaunchedEffect(Unit) {
        mockViewModel.onEvent(PublicCollectionsScreenEvent.LoadInitial)
        delay(RESPONSE_DELAY)
        mockViewModel.onEvent(PublicCollectionsScreenEvent.LoadNextPage)
    }

    ProvidePreviewCompositionLocals {
        ScreenUI(
            listState = lazyListState,
            state = state,
            onEvent = mockViewModel::onEvent
        )
    }
}

typealias ViewModelCreator = () -> ViewModel?

class ViewModelFactory(
    private val viewModelCreator: ViewModelCreator = { null }
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModelCreator() as T
}

@Composable
inline fun <reified VM : ViewModel> viewModelCreator(noinline creator: ViewModelCreator): VM =
    viewModel(factory = remember { ViewModelFactory(creator) })

private const val RESPONSE_THRESHOLD = 8
private const val RESPONSE_DELAY = 1500L