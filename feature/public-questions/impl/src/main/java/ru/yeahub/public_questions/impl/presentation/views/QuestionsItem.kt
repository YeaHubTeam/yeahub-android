package ru.yeahub.public_questions.impl.presentation.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.yeahub.core_ui.component.HideQuestion
import ru.yeahub.public_questions.impl.presentation.screen.QuestionUiModel

@Composable
fun QuestionsItem(
    questions: QuestionUiModel,
    onClickMore: (itemId: String, title: String) -> Unit,
) {
    HideQuestion(
        questionText = questions.title,
        ratingValue = "",
        difficultyValue = "",
        answerText = "",
        imageUrl = "",
        isExtended = false,
        onClickMore = { onClickMore(questions.id.toString(), questions.title) },
        onToggle = {},
        modifier = Modifier
    )
}
