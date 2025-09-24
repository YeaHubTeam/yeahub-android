package ru.yeahub.example_home.impl.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.component.QuestionCard
import ru.yeahub.core_ui.example.dynamicPreview.StandardScreenSizePreview
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.common.observe
import ru.yeahub.example_home.impl.presentation.intents.QuestionMainScreenCommand
import ru.yeahub.example_home.impl.presentation.intents.QuestionMainScreenEvent
import ru.yeahub.example_home.impl.presentation.model.QuestionMainItemType
import ru.yeahub.example_home.impl.presentation.model.QuestionMainUiModel
import ru.yeahub.example_home.impl.presentation.state.QuestionMainScreenState
import ru.yeahub.example_home.impl.presentation.viewmodel.QuestionMainViewModel
import ru.yeahub.ui.R

@Composable
fun QuestionsMainScreen(
    onBackClick: () -> Unit,
    onNavigateToBaseQuestions: () -> Unit,
    onNavigateToCollections: () -> Unit
) {
    val viewModel: QuestionMainViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.command.observe { command ->
        when (command) {
            is QuestionMainScreenCommand.NavigateToBaseQuestions -> onNavigateToBaseQuestions()
            is QuestionMainScreenCommand.NavigateToCollections -> onNavigateToCollections()
        }
    }

    QuestionsMainScreenContent(
        state = state,
        onItemClick = { item -> viewModel.onEvent(QuestionMainScreenEvent.OnItemClick(item)) },
        onBackClick = onBackClick
    )
}

@Composable
fun QuestionsMainScreenContent(
    state: QuestionMainScreenState,
    onItemClick: (QuestionMainUiModel) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    when (state) {
        is QuestionMainScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                QuestionsMainScreenLoading()
            }
        }

        is QuestionMainScreenState.Error -> {
            ErrorScreen(
                error = state.message.getString(context),
                titleText = TextOrResource.Resource(R.string.error_screen_title_text),
                backText = TextOrResource.Resource(R.string.on_back_button_text),
                unknownErrorText = TextOrResource.Resource(R.string.unknown_error_screen_text),
                onBack = onBackClick,
                errorText = TextOrResource.Resource(R.string.error_screen_text),
            )
        }

        //Основной контент
        is QuestionMainScreenState.Content -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp)
            ) {
                //Заголовок
                Text(
                    text = stringResource(id = R.string.question_title),
                    style = Theme.typography.head5,
                    color = Theme.colors.black900,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                //Описание
                Text(
                    text = stringResource(id = R.string.question_description),
                    style = Theme.typography.body7,
                    color = Theme.colors.black900,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                //Кнопки для 'База вопросов' & 'Коллекции'
                state.items.forEach { item ->
                    QuestionCard(
                        title = item.title.getString(context),
                        description = item.description.getString(context),
                        imageRes = item.imageRes,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}

@StaticPreview
@Composable
fun StaticQuestionMainScreenPreview_Loading() {
    QuestionsMainScreenContent(
        state = QuestionMainScreenState.Loading,
        onItemClick = {},
        onBackClick = {}
    )
}

@StaticPreview
@Composable
fun StaticQuestionMainScreenPreview_Content() {
    QuestionsMainScreenContent(
        state = stateWithContent,
        onItemClick = {},
        onBackClick = {}
    )
}

@StaticPreview
@Composable
fun StaticQuestionMainScreenPreview_Error() {
    QuestionsMainScreenContent(
        state = stateWithError,
        onItemClick = {},
        onBackClick = {}
    )
}

@StandardScreenSizePreview
@Composable
fun DynamicQuestionMainScreenPreview_Loading() {
    QuestionsMainScreenContent(
        state = QuestionMainScreenState.Loading,
        onItemClick = {},
        onBackClick = {}
    )
}

@StandardScreenSizePreview
@Composable
fun DynamicQuestionMainScreenPreview_Content() {
    QuestionsMainScreenContent(
        state = stateWithContent,
        onItemClick = {},
        onBackClick = {}
    )
}

@StandardScreenSizePreview
@Composable
fun DynamicQuestionMainScreenPreview_Error() {
    QuestionsMainScreenContent(
        state = stateWithError,
        onItemClick = {},
        onBackClick = {}
    )
}

val stateWithContent = QuestionMainScreenState.Content(
    listOf(
        QuestionMainUiModel(
            type = QuestionMainItemType.BaseQuestions,
            title = TextOrResource.Resource(R.string.base_questions_title),
            description = TextOrResource.Resource(R.string.base_questions_description),
            imageRes = R.drawable.icon_base_question
        ),
        QuestionMainUiModel(
            type = QuestionMainItemType.Collections,
            title = TextOrResource.Resource(R.string.collections_title),
            description = TextOrResource.Resource(R.string.collections_description),
            imageRes = R.drawable.icon_collections
        )
    )
)

val stateWithError = QuestionMainScreenState.Error(
    message = TextOrResource.Resource(R.string.error_screen_text)
)
