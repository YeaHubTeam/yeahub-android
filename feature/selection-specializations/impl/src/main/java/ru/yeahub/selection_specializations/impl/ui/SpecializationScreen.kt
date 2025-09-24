package ru.yeahub.selection_specializations.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.SpecializationButton
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_ui.example.dynamicPreview.ProvidePreviewCompositionLocals
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.colors
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.common.observe
import ru.yeahub.selection_specializations.impl.R
import ru.yeahub.selection_specializations.impl.domain.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.domain.GetSpecializationListUseCase
import ru.yeahub.selection_specializations.impl.domain.SpecializationsRequest
import ru.yeahub.selection_specializations.impl.presentation.SpecializationScreenEvent
import ru.yeahub.selection_specializations.impl.presentation.SpecializationScreenState
import ru.yeahub.selection_specializations.impl.presentation.SpecializationSelectionScreenCommand
import ru.yeahub.selection_specializations.impl.presentation.SpecializationViewModel
import ru.yeahub.selection_specializations.impl.presentation.VoSpecilialization
import ru.yeahub.selection_specializations.impl.presentation.mockResponse
import timber.log.Timber

val FIGMA_HORIZONTAL_PADDING = 16.dp
val FIGMA_VERTICAL_TITLE_PADDING = 22.dp
val FIGMA_VERTICAL_CARD_PADDING = 16.dp //need edge_to_edge = 16

@Composable
fun SpecializationScreen(
    headerText: TextOrResource = TextOrResource.Resource(R.string.selection_specializations_list_header),
    onResult: (SpecializationsScreenResult) -> Unit
) {
    val specializationViewModel: SpecializationViewModel = koinViewModel()
    val screenState by specializationViewModel.screenState.collectAsStateWithLifecycle()
    val lazyListState: LazyListState = rememberLazyListState()

    LaunchedEffect(lazyListState, screenState) {
        snapshotFlow { lazyListState.layoutInfo }
            .map { layoutInfo ->
                val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
                val totalItems = layoutInfo.totalItemsCount
                val isAlmostAtEnd =
                    lastItem != null && totalItems > 0 && lastItem.index >= totalItems - RESPONSE_THRESHOLD
                isAlmostAtEnd
            }.filter { shouldTriggerLoad -> shouldTriggerLoad }
            .collect {
                val canLoadMore = when (
                    val currentScreenState = specializationViewModel.pagerState.value
                ) {
                    is SpecializationScreenState.Loaded -> {
                        !currentScreenState.isEndReached && !currentScreenState.isLoadingNextPage
                    }

                    else -> false
                }
                if (canLoadMore) {
                    specializationViewModel.onEvent(SpecializationScreenEvent.LoadNextPage)
                }
            }
    }

    //command handler
    HandleCommand(
        commandFlow = specializationViewModel.commands,
        onResult = onResult,
    )

    ScreenUI(
        headerText = headerText,
        listState = lazyListState,
        screenState = screenState,
        onSpecialEvent = specializationViewModel::onEvent
    )
}

@Composable
fun BaseSpecializationsScreen(
    modifier: Modifier = Modifier,
    headerText: TextOrResource,
    onItemClick: (id: Long, title: String) -> Unit,
    isPagerLoading: Boolean,
    list: List<VoSpecilialization>,
    listState: LazyListState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.black10),
    ) {
        val context = LocalContext.current

        Text(
            modifier = modifier.padding(
                vertical = FIGMA_VERTICAL_TITLE_PADDING,
                horizontal = FIGMA_HORIZONTAL_PADDING
            ),
            style = LocalAppTypography.current.body5Strong,
            text = headerText.getString(context),
        )

        LazyColumn(
            modifier = modifier.padding(horizontal = FIGMA_HORIZONTAL_PADDING),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(FIGMA_VERTICAL_CARD_PADDING)
        ) {
            items(
                items = list,
                key = { it.id }
            ) { specialization ->
                SpecializationButton(
                    title = TextOrResource.Text(specialization.title),
                    onButtonClick = {
                        onItemClick(
                            specialization.id.toLong(),
                            specialization.title
                        )
                    }
                )
            }
        }

        //shimmer bottom part
        if (isPagerLoading) {
            PrimaryButton(
                modifier = modifier
                    .fillMaxWidth()
                    .shimmer()
                    .background(colors.purple900),
                shape = RoundedCornerShape(0.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {}
            ) {}
        }
    }
}

@Composable
fun ScreenUI(
    headerText: TextOrResource,
    listState: LazyListState,
    screenState: SpecializationScreenState,
    onSpecialEvent: (SpecializationScreenEvent) -> Unit
) {
    Scaffold(
        containerColor = colors.black10,
        topBar = {
            TopAppBarWithBottomBorder(
                title = headerText,
                onBackClick = { onSpecialEvent(SpecializationScreenEvent.OnBackClick) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (screenState) {
                is SpecializationScreenState.Loaded -> {
                    if (
                        screenState.resultList.isEmpty() &&
                        !screenState.isLoadingNextPage &&
                        screenState.isEndReached
                    ) {
                        val padding = PaddingValues(
                            vertical = FIGMA_VERTICAL_TITLE_PADDING,
                            horizontal = FIGMA_HORIZONTAL_PADDING
                        )
                        SpecializationsLoadingScreen(padding = padding)
                    } else {
                        BaseSpecializationsScreen(
                            headerText = headerText,
                            onItemClick = { id, title ->
                                onSpecialEvent(
                                    SpecializationScreenEvent.OnSpecialClick(
                                        id = id,
                                        title = title
                                    )
                                )
                            },
                            isPagerLoading = screenState.isLoadingNextPage,
                            listState = listState,
                            list = screenState.resultList
                        )
                    }
                }

                //difficult to rewrite with res-string
                is SpecializationScreenState.Error -> {
                    val context = LocalContext.current
                    val defaultErrorText = TextOrResource.Text("... no message about throwable")

                    ErrorScreen(
                        error = screenState.throwable.message
                            ?: defaultErrorText.getString(context),
                        titleText = TextOrResource.Text("Crash").getString(context),
                        backText = TextOrResource.Text("Back").getString(context),
                        unknownErrorText = TextOrResource.Text("Something went wrong...")
                            .getString(context),
                        onBack = { SpecializationsScreenResult.NavigateBack }
                    )
                }

                SpecializationScreenState.InitLoading -> {
                    val padding = PaddingValues(
                        vertical = FIGMA_VERTICAL_TITLE_PADDING,
                        horizontal = FIGMA_HORIZONTAL_PADDING
                    )
                    SpecializationsLoadingScreen(padding = padding)
                }
            }
        }
    }
}

@Composable
fun HandleCommand(
    commandFlow: Flow<SpecializationSelectionScreenCommand>,
    onResult: (SpecializationsScreenResult) -> Unit,
) {
    commandFlow.observe { command ->
        when (command) {
            is SpecializationSelectionScreenCommand.OnBackClick -> {
                onResult(SpecializationsScreenResult.NavigateBack)
            }
            is SpecializationSelectionScreenCommand.SpecializationSelectionClick -> {
                onResult(
                    SpecializationsScreenResult.SpecializationClick(
                        command.onClickedSpecId,
                        command.onClickedSpecTitle
                    )
                )
            }
        }
    }
}

//preview part
data class SpecializationScreenParams(
    val screenState: SpecializationScreenState
)

class ListSpecializationScreenProvider : PreviewParameterProvider<SpecializationScreenParams> {
    val names = listOf("React", "Frontend", "QA", "Java")
    val exampleList =
        mutableListOf(
            VoSpecilialization(
                id = -1,
                title = "tooooo looooong speeeeeeeeeeeeeeeeeeeee"
            )
        ).apply {
            for (i in 0 until 15) {
                this.add(VoSpecilialization(id = i, title = names[i % names.size]))
            }
        }

    override val values: Sequence<SpecializationScreenParams> = sequenceOf(
        //with paging loading
        SpecializationScreenParams(
            screenState = SpecializationScreenState.Loaded(
                resultList = exampleList,
                isEndReached = false,
                isLoadingNextPage = true,
            )
        ),
        SpecializationScreenParams(
            screenState = SpecializationScreenState.Error(
                currentList = listOf(),
                throwable = Throwable("Не удалось загрузить данные")
            )
        ),
        SpecializationScreenParams(
            screenState = SpecializationScreenState.InitLoading
        )
    )
}

@StaticPreview
@Composable
fun SpecializationsScreenPreview(
    @PreviewParameter(ListSpecializationScreenProvider::class) params: SpecializationScreenParams
) {
    ScreenUI(
        headerText = TextOrResource.Resource(R.string.selection_specializations_list_header),
        screenState = params.screenState,
        onSpecialEvent = { _ -> Timber.tag("Preview").d("pressed item") },
        listState = rememberLazyListState()
    )
}

@Preview
@Composable
fun SpecializationDynamicPreview() {
    val mockSpecializationUseCase = object : GetSpecializationListUseCase {
        override suspend fun invoke(
            request: SpecializationsRequest
        ): DomainSpecilializationListResponse =
            mockResponse.copy(
                page = request.page.toLong(),
                limit = request.limit.toLong()
            )
    }

    val mockSpecializationViewModel: SpecializationViewModel = viewModelCreator {
        SpecializationViewModel(mockSpecializationUseCase)
    }

    LaunchedEffect(Unit) {
        mockSpecializationViewModel.onEvent(SpecializationScreenEvent.LoadInitial)
        delay(RESPONSE_DELAY)
        mockSpecializationViewModel.onEvent(SpecializationScreenEvent.LoadNextPage)
    }

    val headerText = TextOrResource.Text("Specializations")

    val mockState by mockSpecializationViewModel.screenState.collectAsState()

    ProvidePreviewCompositionLocals {
        ScreenUI(
            headerText = headerText,
            screenState = mockState,
            onSpecialEvent = mockSpecializationViewModel::onEvent,
            listState = rememberLazyListState()
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

private const val RESPONSE_DELAY = 2000L
private const val RESPONSE_THRESHOLD = 8