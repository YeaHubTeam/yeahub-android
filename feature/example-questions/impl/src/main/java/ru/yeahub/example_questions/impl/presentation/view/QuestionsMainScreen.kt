package ru.yeahub.example_questions.impl.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.core_ui.component.QuestionCard
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.example_questions.impl.presentation.ErrorScreen
import ru.yeahub.example_questions.impl.presentation.PlaceholderItem
import ru.yeahub.example_questions.impl.presentation.intents.QuestionMainScreenCommand
import ru.yeahub.example_questions.impl.presentation.intents.QuestionMainScreenEvent
import ru.yeahub.example_questions.impl.presentation.state.QuestionMainScreenState
import ru.yeahub.example_questions.impl.presentation.viewmodel.QuestionMainViewModel
import ru.yeahub.ui.R

@Composable
fun QuestionsMainScreen(
    onBackClick: () -> Unit,
    onNavigateToBaseQuestions: () -> Unit,
    onNavigateToCollections: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: QuestionMainViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.command.collect { cmd ->
            when (cmd) {
                is QuestionMainScreenCommand.NavigateToBaseQuestions -> onNavigateToBaseQuestions()
                is QuestionMainScreenCommand.NavigateToCollections -> onNavigateToCollections()
            }
        }
    }

    when (state) {
        is QuestionMainScreenState.Loading -> {
            Box(
                Modifier.Companion.fillMaxSize(),
                contentAlignment = Alignment.Companion.Center
            ) {
                PlaceholderItem() //todo нужен не Item
            }
        }

        is QuestionMainScreenState.Error -> {
            ErrorScreen(
                error = Throwable("Ошибка загрузки"),
                onRetry = {
                    viewModel.getInitialState()
                }
            )
        }

        //Основной контент
        is QuestionMainScreenState.Content -> {
            val content = state as QuestionMainScreenState.Content

            Column(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .padding(top = 24.dp)
            ) {
                //Заголовок
                Text(
                    text = stringResource(id = R.string.question_title),
                    style = Theme.typography.head5,
                    color = Theme.colors.black900,
                    modifier = Modifier.Companion.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.Companion.height(8.dp))
                //Описание
                Text(
                    text = stringResource(id = R.string.question_description),
                    style = Theme.typography.body7,
                    color = Theme.colors.black900,
                    modifier = Modifier.Companion.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.Companion.height(16.dp))

                //Кнопки для 'База вопросов' & 'Коллекции'
                content.items.forEach { item ->
                    QuestionCard(
                        title = item.title.getString(context),
                        description = item.description.getString(context),
                        imageRes = item.imageRes,
                        onClick = {
                            viewModel.onEvent(QuestionMainScreenEvent.OnItemClick(item))
                        }
                    )
                }
            }
        }
    }
}