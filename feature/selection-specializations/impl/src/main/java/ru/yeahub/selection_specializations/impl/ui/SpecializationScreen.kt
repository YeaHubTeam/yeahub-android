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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.flow.Flow
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
import ru.yeahub.navigation_api.FeatureRoute
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
    parentRoute: String,
    specializationViewModel: SpecializationViewModel,
    onResult: (SpecializationsScreenResult) -> Unit
) {
    val screenState by specializationViewModel.uiStatus.collectAsStateWithLifecycle()

    //command handler
    HandleCommand(
        commandFlow = specializationViewModel.commands,
        onResult = onResult,
    )

    ScreenUI(
        headerText = headerText,
        screenState = screenState,
        onSpecialClick = { id ->
            specializationViewModel.onEvent(
                SpecializationScreenEvent.OnSpecialClick(id = id)
            )
        }
    )
}

@Composable
fun BaseSpecializationsScreen(
    modifier: Modifier = Modifier,
    headerText: TextOrResource,
    onSpecialClick: (id: Int) -> Unit,
    isPagerLoading: Boolean,
    list: List<VoSpecilialization>
) {
    val lazyListState = rememberLazyListState()

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
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(FIGMA_VERTICAL_CARD_PADDING)
        ) {
            items(
                items = list,
                key = { it.id }
            ) { specialization ->
                SpecializationButton(
                    title = TextOrResource.Text(specialization.title),
                    onSpecialClick = onSpecialClick
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
    screenState: SpecializationScreenState,
    onSpecialClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarWithBottomBorder(
                title = headerText,
                onBackClick = { SpecializationsScreenResult.NavigateBack }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (screenState) {
                is SpecializationScreenState.Loaded -> {
                    BaseSpecializationsScreen(
                        headerText = headerText,
                        list = screenState.resultList,
                        isPagerLoading = screenState.isLoadingNextPage,
                        onSpecialClick = onSpecialClick
                    )
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
                onResult(SpecializationsScreenResult.SpecializationClick(command.onClickedSpecId))
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
        onSpecialClick = { id -> println("pressed id=$id") }
    )
}

@Composable
fun MockSpecializationViewModel(): SpecializationViewModel {
    val mockUseCase = remember {
        object : GetSpecializationListUseCase {
            override suspend fun invoke(
                request: SpecializationsRequest
            ): DomainSpecilializationListResponse =
                mockResponse.copy(
                    page = request.page.toLong(),
                    limit = request.limit.toLong()
                )
        }
    }

    return remember { SpecializationViewModel(mockUseCase) }
}

@Preview
@Composable
fun SpecializationDynamicPreview() {
    val parentRoute = FeatureRoute.createFeatureRoute(
        parentRoute = "main screen",
        featureName = "collections"
    )

    fun mockNextRoute(id: String) = FeatureRoute.createFeatureRoute(
        parentRoute = parentRoute,
        featureName = "$parentRoute with specialization id=$id"
    )

    val mockOnResult: (SpecializationsScreenResult) -> Unit = { result ->
        when (result) {
            SpecializationsScreenResult.NavigateBack -> {
                Timber.d("MockBaseSpecializationScreen - nav back to $parentRoute")
            }

            is SpecializationsScreenResult.SpecializationClick -> {
                Timber.d("MockBaseSpecializationScreen - nav to ${mockNextRoute(result.specId)}")
            }
        }
    }

    val headerText = TextOrResource.Text("Selection specializations after $parentRoute")

    ProvidePreviewCompositionLocals {
        SpecializationScreen(
            headerText = headerText,
            parentRoute = parentRoute,
            specializationViewModel = MockSpecializationViewModel(),
            onResult = mockOnResult
        )
    }
}