package ru.yeahub.public_questions.impl.presentation.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.yeahub.core_ui.component.HideQuestion
import ru.yeahub.core_ui.example.dynamicPreview.StandardScreenSizePreview
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.public_questions.impl.presentation.screen.QuestionUiModel

@Composable
fun QuestionsItem(
    questions: List<QuestionUiModel>,
    onClickMore: (itemId: String, title: String) -> Unit,
    onBackClick: () -> Unit
) {
    val extendedStates = remember {
        mutableStateOf(
            value = mutableMapOf<Long, Boolean>()
                .apply {
                    questions.forEach { this[it.id] = false }
                }
        )
    }

    fun toggleExtendedState(id: Long) {
        val currentState = extendedStates.value
        extendedStates.value = currentState.toMutableMap().apply {
            this[id] = !(this[id] ?: false)
        }
    }
    Card(
        modifier = Modifier.padding(
            top = FIGMA_PADDING_TOP.dp,
            bottom = FIGMA_PADDING_BOTTOM.dp,
            start = FIGMA_PADDING_START.dp,
            end = FIGMA_PADDING_END.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            items(questions, key = { it.id }) { question ->
                val isExtended = extendedStates.value[question.id] ?: false
                HideQuestion(
                    questionText = question.title,
                    ratingValue = question.rate,
                    difficultyValue = question.complexity,
                    answerText = question.description,
                    imageUrl = question.imageSc,
                    isExtended = isExtended,
                    onClickMore = { onClickMore(question.id.toString(), question.title) },
                    onToggle = { toggleExtendedState(question.id) },
                    modifier = Modifier
                )
            }
        }
    }
}

/**
 * Static preview
 */
@StaticPreview
@Composable
fun QuestionsItemPreview() {
    val mockQuestions = listOf(
        QuestionUiModel(
            id = 1,
            title = "Что такое Virtual DOM?",
            rate = "5",
            complexity = "Средняя",
            imageSc = "ic_virtual_dom",
            description = "Это концепция, используемая в React для оптимизации рендеринга."
        ),
        QuestionUiModel(
            id = 2,
            title = "Что такое Redux?",
            rate = "",
            complexity = "Высокая",
            imageSc = "",
            description = "Менеджер состояния для JavaScript-приложений."
        ),
        QuestionUiModel(
            id = 3,
            title = "Что такое Kotlin?",
            rate = "10",
            complexity = "Низкая",
            imageSc = "",
            description = "Современный язык программирования для JVM, совместимый с Java."
        ),
        QuestionUiModel(
            id = 4,
            title = "Что такое TypeScript?",
            rate = "",
            complexity = "Средняя",
            imageSc = "",
            description = "Это надмножество JavaScript с типизацией."
        ),
        QuestionUiModel(
            id = 5,
            title = "Что такое функциональное программирование?",
            rate = "10",
            complexity = "Очень высокая",
            imageSc = "ic_functional_programming",
            description = "Это парадигма программирования," +
                    " где функции являются основными строительными блоками."
        ),
        QuestionUiModel(
            id = 6,
            title = "Что такое Monads в функциональном программировании?",
            rate = "8",
            complexity = "Очень высокая",
            imageSc = "",
            description = "Это концепция, которая помогает управлять побочными эффектами" +
                    " в функциональном программировании."
        ),
        QuestionUiModel(
            id = 7,
            title = "Что такое Java?",
            rate = "7",
            complexity = "Средняя",
            imageSc = "ic_java",
            description = "Это язык программирвания"
        )
    )

    QuestionsItem(
        questions = mockQuestions,
        onClickMore = { itemId, title -> },
        onBackClick = {}
    )
}

private const val FIGMA_PADDING_BOTTOM = 20
private const val FIGMA_PADDING_TOP = 20
private const val FIGMA_PADDING_START = 10
private const val FIGMA_PADDING_END = 10

/**
 * Dinamic preview
 */
class MockQuestionsViewModel : ViewModel() {
    private val _state = MutableStateFlow<List<QuestionUiModel>>(emptyList())
    val state: Flow<List<QuestionUiModel>> get() = _state.asStateFlow()

    init {
        _state.value = List(TEST) { index ->
            QuestionUiModel(
                id = index.toLong(),
                title = "Mocked Question Title $index",
                rate = "Rate: ${index * 3}",
                complexity = "Complexity: ${index + 1}",
                imageSc = "https://via.placeholder.com/150",
                description = "Mocked description for question #$index"
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
        onBackClick = {}
    )
}
