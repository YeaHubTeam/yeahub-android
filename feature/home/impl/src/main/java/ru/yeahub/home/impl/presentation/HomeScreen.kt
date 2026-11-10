package ru.yeahub.home.impl.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.core_ui.example.dynamicPreview.StandardScreenSizePreview
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.home.impl.R
import ru.yeahub.home.impl.domain.interview.InterviewData
import ru.yeahub.home.impl.presentation.intents.InterviewPreparationEvent
import ru.yeahub.home.impl.presentation.screen.InterviewPreparationBlock
import ru.yeahub.home.impl.presentation.screen.InterviewPreparationPlaceholder
import ru.yeahub.home.impl.presentation.screen.InterviewPreparationState
import ru.yeahub.home.impl.presentation.viewmodel.InterviewPreparationViewModel

/**
 * Главная страница — композиция независимых блоков.
 * Пока реализован блок тренажёра собеседований: одна плашка
 * со случайной записью из мока (задержка загрузки 1–3 секунды).
 */
@Composable
fun HomeScreen() {
    val viewModel: InterviewPreparationViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Паддинги статус-бара и нижней навигации уже применены внешним Scaffold'ом
    // в AppNavigation (Box(Modifier.padding(padding)) вокруг NavHost) —
    // здесь повторно их применять не нужно, иначе низ списка получит двойной отступ.
    Scaffold { _ ->
        HomeScreenContent(
            state = state,
            onRetry = { viewModel.onEvent(InterviewPreparationEvent.Retry) },
            onPassClick = {
                // TODO: навигация к прохождению собеседования (onResult -> FeatureImpl)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun HomeScreenContent(
    state: InterviewPreparationState,
    onRetry: () -> Unit,
    onPassClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        MainHeader()
        when (state) {
            is InterviewPreparationState.Initial,
            is InterviewPreparationState.Loading -> InterviewPreparationPlaceholder()

            is InterviewPreparationState.Loaded -> InterviewPreparationBlock(
                currentProgress = state.interview.currentProgress,
                allProgress = state.interview.allProgress,
                currentQuestion = state.interview.currentQuestion,
                onPassClick = onPassClick
            )

            is InterviewPreparationState.Error -> HomeErrorState(onRetry = onRetry)
        }
    }
}

@Composable
private fun HomeErrorState(onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.home_loading_error),
            style = Theme.typography.body1,
            color = Theme.colors.black900
        )
        TextButton(onClick = onRetry) {
            Text(text = stringResource(R.string.home_retry))
        }
    }
}

@Composable
private fun MainHeader(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        Text(
            stringResource(R.string.home_header_title),
            style = Theme.typography.head5,
            color = Theme.colors.black900
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .size(48.dp)
                .background(Theme.colors.purple300)
        )
    }
}

private val previewInterview = InterviewData(
    currentProgress = 23,
    allProgress = 45,
    currentQuestion = "Что такое data class и для чего он нужен?"
)

@StaticPreview
@Composable
internal fun StaticHomeScreenPreview_Loading() {
    YeaHubTheme {
        HomeScreenContent(
            state = InterviewPreparationState.Loading,
            onRetry = {},
            onPassClick = {}
        )
    }
}

@StaticPreview
@Composable
internal fun StaticHomeScreenPreview_Content() {
    YeaHubTheme {
        HomeScreenContent(
            state = InterviewPreparationState.Loaded(previewInterview),
            onRetry = {},
            onPassClick = {}
        )
    }
}

@StaticPreview
@Composable
internal fun StaticHomeScreenPreview_Error() {
    YeaHubTheme {
        HomeScreenContent(
            state = InterviewPreparationState.Error(IllegalStateException("Нет соединения")),
            onRetry = {},
            onPassClick = {}
        )
    }
}

@StandardScreenSizePreview
@Composable
internal fun HomeScreenContentPreview() {
    YeaHubTheme {
        HomeScreenContent(
            state = InterviewPreparationState.Loaded(previewInterview),
            onRetry = {},
            onPassClick = {}
        )
    }
}
