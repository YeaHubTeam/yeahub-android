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
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.observe
import ru.yeahub.example_home.impl.presentation.intents.QuestionMainScreenCommand
import ru.yeahub.example_home.impl.presentation.intents.QuestionMainScreenEvent
import ru.yeahub.example_home.impl.presentation.state.QuestionMainScreenState
import ru.yeahub.example_home.impl.presentation.viewmodel.QuestionMainViewModel
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

        viewModel.command.observe { command ->
            when (command) {
                is QuestionMainScreenCommand.NavigateToBaseQuestions -> onNavigateToBaseQuestions()
                is QuestionMainScreenCommand.NavigateToCollections -> onNavigateToCollections()
            }
        }

    when (state) {
        is QuestionMainScreenState.Loading -> {
            Box(
                Modifier.Companion.fillMaxSize(),
                contentAlignment = Alignment.Companion.Center
            ) {
//                PlaceholderItem() //todo нужен экран при загрузке
            }
        }

        is QuestionMainScreenState.Error -> {
            ErrorScreen(
                error = (state as QuestionMainScreenState.Error).message.getString(context),
                titleText = "Ошибка загрузки",
                backText = "",
                unknownErrorText = "",
                onBack = { }
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