package ru.yeahub.selection_specializations.impl.ui

import SpecializationsScreenApi
import SpecializationsScreenResult
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.SpecializationButton
import ru.yeahub.core_ui.example.dynamicPreview.ProvidePreviewCompositionLocals
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.colors
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.common.observe
import ru.yeahub.navigation_api.FeatureRoute
import ru.yeahub.selection_specializations.impl.R
import ru.yeahub.selection_specializations.impl.domain.GetSpecializationListUseCase
import ru.yeahub.selection_specializations.impl.dynamic_preview.mockResponse
import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.model.SpecializationsRequest
import ru.yeahub.selection_specializations.impl.model.VoSpecilialization
import ru.yeahub.selection_specializations.impl.presentation.SpecializationScreenEvent
import ru.yeahub.selection_specializations.impl.presentation.SpecializationScreenState
import ru.yeahub.selection_specializations.impl.presentation.SpecializationSelectionScreenCommand
import ru.yeahub.selection_specializations.impl.presentation.SpecializationViewModel
import timber.log.Timber

class SpecializationScreen : SpecializationsScreenApi {
    //TODO: TOP BAR LIKE PUBLIC QUESTION

    companion object {
        val FIGMA_HORIZONTAL_PADDING = 16.dp
        val FIGMA_VERTICAL_TITLE_PADDING = 22.dp
        val FIGMA_VERTICAL_CARD_PADDING = 16.dp //need edge_to_edge = 16
    }

    @SuppressLint("NotConstructor")
    @Composable
    override fun SpecializationScreen(
        modifier: Modifier,
        headerText: TextOrResource,
        parentRoute: String,
        onResult: (SpecializationsScreenResult) -> Unit,
    ) {
        val specialViewModel: SpecializationViewModel = koinViewModel()

        SpecializationScreenWithViewModel(
            modifier = modifier,
            viewModel = specialViewModel,
            parentRoute = parentRoute,
            onResult = onResult
        )
    }

    @Composable
    fun SpecializationScreenWithViewModel(
        modifier: Modifier = Modifier,
        viewModel: SpecializationViewModel,
        parentRoute: String,
        onResult: (SpecializationsScreenResult) -> Unit,
    ) {
        val screenState = viewModel.uiStatus.collectAsStateWithLifecycle()

        //command handler
        HandleCommand(
            commandFlow = viewModel.commands,
            onResult = onResult,
        )

        when (screenState) {
            is SpecializationScreenState.Loaded -> {
                BaseSpecializationsScreen(
                    modifier = modifier,
                    list = screenState.resultList,
                    isPagerLoading = screenState.isLoadingNextPage,
                    onSpecialClick = { id ->
                        viewModel.onEvent(
                            SpecializationScreenEvent.OnSpecialClick(id = id)
                        )
                    }
                )
            }

            //difficult to rewrite with res-string
            is SpecializationScreenState.Error -> {
                val context = LocalContext.current
                val defaultErrorText = TextOrResource.Text("... no message about throwable").getString(context)

                ErrorScreen(
                    error = screenState.throwable.message ?: defaultErrorText,
                    titleText = TextOrResource.Text("Crash").getString(context),
                    backText = TextOrResource.Text("Back").getString(context),
                    unknownErrorText = TextOrResource.Text("Something went wrong...").getString(context),
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

    @Composable
    fun BaseSpecializationsScreen(
        modifier: Modifier = Modifier,
        onSpecialClick: (id: Int) -> Unit,
        isPagerLoading: Boolean,
        list: List<VoSpecilialization>
    ) {
        val lazyListState = rememberLazyListState()
        val context = LocalContext.current

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
                style = LocalAppTypography.current.body5Strong,
                text =
                    TextOrResource
                        .Resource(R.string.selection_specializations_list_header)
                        .getString(context),
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

    //with paging loading
    @StaticPreview
    @Composable
    fun SpecializationsScreenPreview() {
        val exampleList =
            mutableListOf(
                VoSpecilialization(
                    id = -1,
                    title = "tooooo looooong speeeeeeeeeeeeeeeeeeeee"
                )
            )
        val names = listOf("React", "Frontend", "QA", "Java")
        for (i in 0 until 15) {
            exampleList.add(VoSpecilialization(id = i, title = names[i % names.size]))
        }

        BaseSpecializationsScreen(
            list = exampleList,
            onSpecialClick = { id -> println("pressed id=$id") },
            isPagerLoading = true
        )
    }

    @SuppressLint("ViewModelConstructorInComposable")
    @Preview
    @Composable
    fun SpecializationDynamicPreview() {
        val parentRoute = FeatureRoute.createFeatureRoute(
            parentRoute = "main screen",
            featureName = "collections"
        )

        fun nextRoute(id: String) = FeatureRoute.createFeatureRoute(
            parentRoute = parentRoute,
            featureName = "$parentRoute with specialization id=$id"
        )

        val mockOnResult: (SpecializationsScreenResult) -> Unit = { result ->
            when (result) {
                SpecializationsScreenResult.NavigateBack -> {
                    Timber.d("MockBaseSpecializationScreen - nav back to $parentRoute")
                }

                is SpecializationsScreenResult.SpecializationClick -> {
                    Timber.d("MockBaseSpecializationScreen - nav to ${nextRoute(result.specId)}")
                }
            }
        }

        ProvidePreviewCompositionLocals {
            SpecializationScreenWithViewModel(
                viewModel = SpecializationViewModel(
                    getSpecializationListUseCase = object : GetSpecializationListUseCase {
                        override suspend fun invoke(
                            request: SpecializationsRequest
                        ): DomainSpecilializationListResponse =
                            mockResponse.copy(
                                page = request.page.toLong(),
                                limit = request.limit.toLong()
                            )
                    }
                ),
                parentRoute =  parentRoute,
                onResult = mockOnResult
            )
        }
    }
}
