package ru.yeahub.public_questions.impl.presentation.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.public_questions.impl.R
import ru.yeahub.public_questions.impl.presentation.intents.PublicQuestionsResult
import ru.yeahub.public_questions.impl.presentation.intents.PublicQuestionsScreenCommand
import ru.yeahub.public_questions.impl.presentation.intents.PublicQuestionsScreenEvent
import ru.yeahub.public_questions.impl.presentation.viewmodel.PublicQuestionsViewModel
import ru.yeahub.public_questions.impl.presentation.views.ErrorItem
import ru.yeahub.public_questions.impl.presentation.views.PlaceholderItem
import ru.yeahub.public_questions.impl.presentation.views.PublicQuestionsItem
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun PublicQuestionsScreen(
    onResult: (PublicQuestionsResult) -> Unit,
    skills: List<String>? = null,
    skillFilter: String? = null,
    heading: String,
    lazyListState: LazyListState = rememberLazyListState()
) {
    val viewModel: PublicQuestionsViewModel = koinViewModel(
        parameters = { parametersOf(skills, skillFilter) }
    )
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    HandlePublicQuestionsCommand(
        commandFlow = viewModel.commandState,
        onResult = { result -> onResult(result) }
    )

    LaunchedEffect(lazyListState, screenState) {
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
                    is PublicQuestionsScreenState.Loaded -> {
                        !currentScreenState.isEndReached && !currentScreenState.isLoadingNextPage
                    }

                    else -> false
                }
                if (canLoadMore) {
                    viewModel.onEvent(PublicQuestionsScreenEvent.LoadNextPage)
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBarWithBottomBorder(
                title = heading,
                onBackClick = { viewModel.onEvent(PublicQuestionsScreenEvent.OnBackClick) }
            )
        }
    ) { padding ->
        PublicQuestionsContent(
            padding = padding,
            screenState = screenState,
            listState = lazyListState,
            onRetryLoadInitial = { viewModel.onEvent(PublicQuestionsScreenEvent.LoadInitial) },
            title = heading,
            onMoreCLick = { id ->
                viewModel.onEvent(
                    PublicQuestionsScreenEvent.OnMoreClick(
                        id = id
                    )
                )
            }
        )
    }
}

@Composable
fun HandlePublicQuestionsCommand(
    commandFlow: Flow<PublicQuestionsScreenCommand>,
    onResult: (PublicQuestionsResult) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = commandFlow, key2 = lifecycleOwner) {
        commandFlow
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { command ->
                when (command) {
                    is PublicQuestionsScreenCommand.OnMoreClick -> {
                        onResult(PublicQuestionsResult.NavigateToDetail(command.id))
                    }

                    PublicQuestionsScreenCommand.OnBackClick -> {
                        onResult(PublicQuestionsResult.NavigateBack)
                    }
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithBottomBorder(
    title: String,
    borderColor: Color = Theme.colors.black50,
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
                color = Theme.colors.black900
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
                    tint = Theme.colors.purple700
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Theme.colors.white900
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
private fun PublicQuestionsContent(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    title: String,
    onMoreCLick: (id: String) -> Unit,
    screenState: PublicQuestionsScreenState,
    listState: LazyListState,
    onRetryLoadInitial: () -> Unit,
) {
    Box(
        modifier = modifier.background(Theme.colors.white900)
    ) {
        when (screenState) {
            is PublicQuestionsScreenState.Initial, PublicQuestionsScreenState.Loading -> {
                FullScreenPlaceholders(
                    listState = listState,
                    nameQuestion = title
                )
            }

            is PublicQuestionsScreenState.Error -> {
                if (screenState.questions.isEmpty()) {
                    FullScreenError(
                        error = screenState.throwable,
                        onRetry = onRetryLoadInitial
                    )
                } else {
                    QuestionsListWithFooter(
                        padding = padding,
                        listState = listState,
                        questions = screenState.questions,
                        isEndReached = true,
                        isLoadingNextPage = false,
                        paginationError = screenState.throwable,
                        onRetryPagination = { onRetryLoadInitial() },
                        nameQuestion = title,
                        onMoreCLick = { id -> onMoreCLick(id) }
                    )
                }
            }

            is PublicQuestionsScreenState.Loaded -> {
                if (screenState.questions
                        .isEmpty() && !screenState.isLoadingNextPage && screenState.isEndReached
                ) {
                    EmptyState()
                } else {
                    QuestionsListWithFooter(
                        padding = padding,
                        listState = listState,
                        questions = screenState.questions,
                        isEndReached = screenState.isEndReached,
                        isLoadingNextPage = screenState.isLoadingNextPage,
                        paginationError = null,
                        onRetryPagination = { onRetryLoadInitial() },
                        nameQuestion = title,
                        onMoreCLick = { id -> onMoreCLick(id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun FullScreenPlaceholders(
    listState: LazyListState,
    nameQuestion: String
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        userScrollEnabled = false
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, bottom = 20.dp, top = 20.dp)
                    .blur(10.dp)
                    .shimmer(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.questions_list_header, nameQuestion),
                    style = Theme.typography.body5Strong
                )
            }
        }
        items(COUNT_PLACEHOLDER) { PlaceholderItem() }
    }
}

@Composable
private fun FullScreenError(
    modifier: Modifier = Modifier,
    error: Throwable,
    onRetry: () -> Unit
) {
    ErrorItem(
        modifier = modifier.fillMaxSize(),
        error = error,
        onRetry = onRetry
    )
}

@Composable
private fun QuestionsListWithFooter(
    padding: PaddingValues,
    nameQuestion: String,
    listState: LazyListState,
    questions: List<PublicQuestionUiModel>,
    isEndReached: Boolean,
    isLoadingNextPage: Boolean,
    paginationError: Throwable?,
    onMoreCLick: (id: String) -> Unit,
    onRetryPagination: () -> Unit,
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize().padding(padding)
    ) {
        item {
            Box(modifier = Modifier.padding(start = 15.dp, bottom = 20.dp, top = 20.dp)) {
                Text(
                    text = stringResource(id = R.string.questions_list_header, nameQuestion),
                    style = Theme.typography.body5Strong,
                    color = Theme.colors.black900
                )
            }
        }
        if (questions.isNotEmpty()) {
            items(
                items = questions,
                key = { question -> question.id }
            ) { question ->
                PublicQuestionsItem(
                    questions = question,
                    onClickMore = { id -> onMoreCLick(id) }
                )
            }
        }

        item {
            PaginationFooter(
                isLoading = isLoadingNextPage,
                isEndReached = isEndReached,
                error = paginationError,
                onRetry = onRetryPagination
            )
        }
    }
}

@Composable
private fun PaginationFooter(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isEndReached: Boolean,
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
                        Text(stringResource(id = R.string.try_again))
                    }
                }
            }

            isEndReached -> {
                Text(stringResource(id = R.string.error))
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
            context.getString(R.string.there_i_no_internet)
        }

        is TimeoutException -> {
            context.getString(
                R.string.the_response_timeout_expired
            )
        }

        else -> throwable.localizedMessage ?: context.getString(R.string.error)
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
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

private const val RESPONSE_THRESHOLD = 8
private const val COUNT_PLACEHOLDER = 11