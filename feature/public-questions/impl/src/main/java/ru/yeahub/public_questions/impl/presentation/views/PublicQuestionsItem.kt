package ru.yeahub.public_questions.impl.presentation.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.yeahub.core_ui.component.HideQuestion
import ru.yeahub.public_questions.impl.presentation.screen.PublicQuestionUiModel

@Composable
fun PublicQuestionsItem(
    questions: PublicQuestionUiModel,
    onClickMore: (itemId: String) -> Unit,
) {
    HideQuestion(
        questionText = questions.title,
        ratingValue = "",
        difficultyValue = "",
        answerText = "",
        imageUrl = "",
        isExtended = false,
        onClickMore = { onClickMore(questions.id.toString()) },
        onToggle = {},
        modifier = Modifier
    )
}
/**
 * Static preview

@StaticPreview
@Composable
fun QuestionsItemPreview() {
val mockQuestions = listOf(
PublicQuestionUiModel(
id = 1,
title = "Что такое Virtual DOM?",
),
PublicQuestionUiModel(
id = 2,
title = "Что такое Redux?",
),
PublicQuestionUiModel(
id = 3,
title = "Что такое Kotlin?",
),
PublicQuestionUiModel(
id = 4,
title = "Что такое TypeScript?",
),
PublicQuestionUiModel(
id = 5,
title = "Что такое функциональное программирование?",
),
PublicQuestionUiModel(
id = 6,
title = "Что такое Monads в функциональном программировании?",
),
PublicQuestionUiModel(
id = 7,
title = "Что такое Java?",
)
)

/* QuestionsItem(
questions = mockQuestions,
onClickMore = { itemId, title -> },

)*/
}

/**
 * Dinamic preview
*/
class MockQuestionsViewModel : ViewModel() {
private val _state = MutableStateFlow<List<PublicQuestionUiModel>>(emptyList())
val state: Flow<List<PublicQuestionUiModel>> get() = _state.asStateFlow()

init {
_state.value = List(TEST) { index ->
PublicQuestionUiModel(
id = index.toLong(),
title = "Mocked Question Title $index",
)
}
}
}

private const val TEST = 10

@StandardScreenSizePreview
@Composable
fun PreviewQuestionsScreen() {
val viewModel: MockQuestionsViewModel = viewModel()
val state = viewModel.state.collectAsState(emptyList())

QuestionsItem(
questions = state.value,
onClickMore = { id, title -> },
)
}**/