package com.example.impl.presentation.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.impl.R
import com.example.impl.presentation.intents.DetailQuestionCommand
import com.example.impl.presentation.mapper.DetailQuestionScreenMapper
import com.example.impl.presentation.viewmodel.DetailQuestionViewModel
import com.example.impl.presentation.viewmodel.publicQuestion
import com.example.impl.presentation.viewmodel.viewModelCreator
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.core_ui.example.dynamicPreview.StandardScreenSizePreview
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme

@Composable
fun DetailQuestionScreen(
    onBackClick: () -> Unit,
) {
    val viewModel: DetailQuestionViewModel = koinViewModel()

    DetailQuestionScreenView(
        onBackClick = onBackClick,
        viewModel = viewModel
    )

    HandleCommands(viewModel.commands)
}

@Composable
internal fun HandleCommands(commands: SharedFlow<DetailQuestionCommand>) {
    TODO()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailQuestionScreenView(
    onBackClick: () -> Unit,
    viewModel: DetailQuestionViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = Theme.colors.black25,
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    modifier = Modifier,
                    title = { },
                    navigationIcon = { },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Theme.colors.white900
                    ),
                    actions = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(bounded = false),
                                    ) { onBackClick() }
                                    .animateContentSize()
                                    .width(IntrinsicSize.Min),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.back_button_icon),
                                    contentDescription = "icon_back",
                                    tint = Theme.colors.purple700,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.back_button_text),
                                    style = Theme.typography.body3Accent,
                                    color = Theme.colors.purple700
                                )
                            }
                        }
                    }
                )
                HorizontalDivider(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(),
                    color = Theme.colors.black50,
                    thickness = 1.dp
                )
            }
        }
    ) { padding ->
        DetailQuestionScreenState(uiState, padding)
    }
}

@Composable
fun DetailQuestionScreenState(
    uiState: DetailQuestionState,
    padding: PaddingValues,
    errorStrings: DetailQuestionStrings = rememberDetailQuestionStrings()
) {
    when (uiState) {
        is DetailQuestionState.Initial -> Unit
        is DetailQuestionState.Success -> QuestionContent(uiState.data, padding, errorStrings)
        is DetailQuestionState.LoadingState -> LoadingScreen(padding)
        is DetailQuestionState.ErrorState -> ErrorScreen(
            uiState.message,
            onRetry = { },
            errorStrings
        )
    }
}

data class DetailQuestionScreenStateParams(
    val state: DetailQuestionState,
    val padding: PaddingValues = PaddingValues(),
    val errorStrings: DetailQuestionStrings = previewErrorScreenStrings()
)

class ListOfDetailQuestionScreenStateProvider :
    PreviewParameterProvider<DetailQuestionScreenStateParams> {

    override val values = sequenceOf(
        DetailQuestionScreenStateParams(DetailQuestionState.LoadingState),
        DetailQuestionScreenStateParams(DetailQuestionState.Success(publicQuestion)),
        DetailQuestionScreenStateParams(DetailQuestionState.ErrorState("Не удалось загрузить вопрос")),
    )
}

@StaticPreview
@Composable
fun ShowDetailQuestionPreview(
    @PreviewParameter(ListOfDetailQuestionScreenStateProvider::class) params: DetailQuestionScreenStateParams
) {
    StatesDetailQuestionPreview(params)
}

@Composable
fun StatesDetailQuestionPreview(params: DetailQuestionScreenStateParams) {
    DetailQuestionScreenState(
        uiState = params.state,
        padding = params.padding,
        errorStrings = params.errorStrings
    )
}

@StandardScreenSizePreview
@Composable
fun DetailQuestionScreenDynamicPreview() {
    val mapper = remember {
        DetailQuestionScreenMapper()
    }
    val mockViewModel: DetailQuestionViewModel = viewModelCreator {
        DetailQuestionViewModel(mapper)
    }
    DetailQuestionScreenView(onBackClick = {}, viewModel = mockViewModel)
}