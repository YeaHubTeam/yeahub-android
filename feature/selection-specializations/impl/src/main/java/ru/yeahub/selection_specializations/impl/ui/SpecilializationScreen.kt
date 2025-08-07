package ru.yeahub.selection_specializations.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.core_ui.component.ErrorScreen
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
            SuccessSpecializationsScreen(
                list = screenState.resultList,
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

        SpecilializationScreenState.PagerLoading -> {}
    }
}

@Composable
fun SuccessSpecializationsScreen(
    modifier: Modifier = Modifier,
    onSpecialClick: (id: Int) -> Unit,
    list: List<VoSpecilialization>
) {
    val lazyListState = rememberLazyListState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.black10)
            .padding(horizontal = FIGMA_HORIZONTAL_PADDING),
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
            modifier = modifier,
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
    }
}

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

    SuccessSpecializationsScreen(
        list = exampleList,
        onSpecialClick = { id -> println("pressed id=$id") }
    )
}