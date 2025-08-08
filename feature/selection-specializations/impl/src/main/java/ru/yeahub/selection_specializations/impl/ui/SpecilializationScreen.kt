package ru.yeahub.selection_specializations.impl.ui

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.shimmer
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.colors
import ru.yeahub.selection_specializations.impl.model.VoSpecilialization
import ru.yeahub.selection_specializations.impl.presentation.HandleSpecialCommand
import ru.yeahub.selection_specializations.impl.presentation.OnSpecialFeatureUseCase
import ru.yeahub.selection_specializations.impl.presentation.SpecilializationScreenEvent
import ru.yeahub.selection_specializations.impl.presentation.SpecilializationScreenState
import ru.yeahub.selection_specializations.impl.presentation.SpecilializationViewModel

val FIGMA_HORIZONTAL_PADDING = 16.dp
val FIGMA_VERTICAL_TITLE_PADDING = 22.dp
val FIGMA_VERTICAL_CARD_PADDING = 16.dp //need edge_to_edge = 16

@Composable
fun SpecializationsScreen(
    modifier: Modifier = Modifier,
    onNavigate: (nextRoute: String) -> Unit,
    currentUseCase: OnSpecialFeatureUseCase,
    onBackClick: () -> Unit
) {
    val specialViewModel: SpecilializationViewModel = koinViewModel()
    val screenState = specialViewModel.uiStatus.collectAsStateWithLifecycle()

    //command handler
    HandleSpecialCommand(
        commandFlow = specialViewModel.commands,
        currentUseCase = currentUseCase,
        onNavigate = onNavigate,
        onBackClick = onBackClick
    )

    when (screenState) {
        is SpecilializationScreenState.Loaded -> {
            BaseSpecializationsScreen(
                list = screenState.resultList,
                isPagerLoading = screenState.isLoadingNextPage,
                onSpecialClick = { id ->
                    specialViewModel.onEvent(
                        SpecilializationScreenEvent.OnSpecialClick(
                            currentUseCase = currentUseCase,
                            id = id
                        )
                    )
                }
            )
        }

        is SpecilializationScreenState.Error -> {
            ErrorScreen(
                error = screenState.throwable.message ?: "... no message about throwable",
                titleText = "Crash",
                backText = "Back",
                unknownErrorText = "Something went wrong...",
                onBack = onBackClick
            )
        }

        SpecilializationScreenState.InitLoading -> {
            val padding = PaddingValues(
                vertical = FIGMA_VERTICAL_TITLE_PADDING,
                horizontal = FIGMA_HORIZONTAL_PADDING
            )
            SpecializationsLoadingScreen(padding = padding)
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.black10),
    ) {
        val titleTextStyle = LocalAppTypography.current.body5Strong
        Text(
            modifier = modifier.padding(
                vertical = FIGMA_VERTICAL_TITLE_PADDING,
                horizontal = FIGMA_HORIZONTAL_PADDING
            ),
            fontSize = titleTextStyle.fontSize,
            fontStyle = titleTextStyle.fontStyle,
            fontWeight = titleTextStyle.fontWeight,
            fontFamily = titleTextStyle.fontFamily,
            text = "IT Specilializations",
        )

        LazyColumn(
            modifier = modifier.padding(horizontal = FIGMA_HORIZONTAL_PADDING),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(FIGMA_VERTICAL_CARD_PADDING)
        ) {
            items(
                items = list,
                key = { it.hashCode() }
            ) { specialization ->
                SpecilializationButton(
                    title = specialization.title,
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
@Preview
@Composable
fun SpecializationsScreenReview() {
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