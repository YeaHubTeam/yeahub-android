package ru.yeahub.interview_trainer.impl.interviewQuiz.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.SecondaryButton
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_ui.component.YeahubButtonDefaults
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.interview_trainer.impl.R
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.InterviewQuizEvent
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.InterviewQuizScreenMapper
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.InterviewQuizState
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.InterviewQuizViewModel

private val FIGMA_MEDIUM_PADDING = 16.dp
private val FIGMA_LOW_PADDING = 8.dp
private val FIGMA_VERTICAL_FIRST_AND_LAST_ELEMENT_PADDING = 24.dp

private val FIGMA_CARD_ELEVATION = 4.dp
private val FIGMA_RADIUS = 12.dp

private data class QuestionCardState(
    val questionText: String,
    val shortAnswer: String,
    val currentAnswer: InterviewQuizState.Loaded.QuizAnswer,
    val isAnswerVisible: Boolean,
    val canGoPrevious: Boolean,
    val canGoNext: Boolean,
    val isLastQuestion: Boolean
)

private fun InterviewQuizState.Loaded.toQuestionCardState(): QuestionCardState {
    val question = questions[currentQuestionIndex]

    return QuestionCardState(
        questionText = question.title,
        shortAnswer = question.shortAnswer,
        currentAnswer = currentAnswer,
        isAnswerVisible = isAnswerVisible,
        canGoPrevious = canGoPrevious,
        canGoNext = canGoNext,
        isLastQuestion = currentQuestionIndex == questions.lastIndex
    )
}

@Composable
private fun ScreenUI(
    headerText: TextOrResource,
    state: InterviewQuizState,
    onEvent: (InterviewQuizEvent) -> Unit
) {

    Scaffold(
        containerColor = Theme.colors.black10,
        topBar = {
            TopAppBarWithBottomBorder(
                title = headerText,
                onBackClick = { TODO("onBackClick don't implemented") }
            )
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            when (state) {
                is InterviewQuizState.Loaded -> BaseQuizScreen(
                    state = state,
                    onPreviousClick = {
                        onEvent(InterviewQuizEvent.OnPreviousQuestionClick)
                    },
                    onNextClick = {
                        onEvent(InterviewQuizEvent.OnNextQuestionClick)
                    },
                    onUnknownClick = {
                        onEvent(InterviewQuizEvent.OnUnknownAnswerClick)
                    },
                    onKnownClick = {
                        onEvent(InterviewQuizEvent.OnKnownAnswerClick)
                    },
                    onShowHideAnswerClick = {
                        onEvent(InterviewQuizEvent.OnShowHideAnswerClick)
                    },
                    onResultClick = {
                        onEvent(InterviewQuizEvent.OnShowResultClick)
                    },
                )

                is InterviewQuizState.Error -> ErrorScreen(
                    error = state.throwable.localizedMessage,
                    errorText = TextOrResource.Resource(R.string.error_screen_text),
                    titleText = TextOrResource.Resource(R.string.title_error_screen_text),
                    backText = TextOrResource.Resource(R.string.back_error_screen_text),
                    unknownErrorText = TextOrResource.Resource(R.string.unknown_error_screen_text),
                    onBack = { TODO() }
                )

                InterviewQuizState.Loading -> InterviewQuizLoading()
            }
        }
    }
}

@Composable
private fun BaseQuizScreen(
    state: InterviewQuizState.Loaded,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onUnknownClick: () -> Unit,
    onKnownClick: () -> Unit,
    onShowHideAnswerClick: () -> Unit,
    onResultClick: () -> Unit
) {

    val cardState = state.toQuestionCardState()

    Column(
        modifier = Modifier.padding(
            start = FIGMA_MEDIUM_PADDING,
            end = FIGMA_MEDIUM_PADDING,
            top = FIGMA_VERTICAL_FIRST_AND_LAST_ELEMENT_PADDING
        )
    ) {
        QuizProgress(
            current = state.currentQuestionIndex + 1,
            total = state.questionsCount,
        )
        Spacer(Modifier.height(FIGMA_VERTICAL_FIRST_AND_LAST_ELEMENT_PADDING))
        QuestionCard(
            state = cardState,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            onUnknownClick = onUnknownClick,
            onKnownClick = onKnownClick,
            onShowAnswerClick = onShowHideAnswerClick,
            onResultClick = onResultClick,
        )
    }
}

@Composable
private fun QuizProgress(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier
) {

    val progress = (current.toFloat() / total)

    DefaultCard(modifier) {
        Column(modifier = Modifier.padding(FIGMA_MEDIUM_PADDING)) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(24)),
                color = Theme.colors.purple700,
                trackColor = Theme.colors.purple300,
                gapSize = (-8).dp,
                strokeCap = StrokeCap.Round,
                drawStopIndicator = {}
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = TextOrResource.Text("$current из $total").text,
                color = Theme.colors.black500,
                style = Theme.typography.body2Accent,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
private fun QuestionCard(
    state: QuestionCardState,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onUnknownClick: () -> Unit,
    onKnownClick: () -> Unit,
    onShowAnswerClick: () -> Unit,
    onResultClick: () -> Unit
) {

    /* TODO: нет фичи профиля */
    var isFavorite by rememberSaveable { mutableStateOf(false) }

    val favoriteIcon: Painter =
        painterResource(
            if (isFavorite) R.drawable.favorite_filled_icon
            else R.drawable.favorite_outlined_icon
        )

    DefaultCard {
        Column(Modifier
            .fillMaxWidth()
            .padding(FIGMA_MEDIUM_PADDING)) {
            Row(Modifier.fillMaxWidth()) {
                NavigationButton(
                    text = TextOrResource.Resource(R.string.quiz_btn_prev),
                    enabled = state.canGoPrevious,
                    onClick = onPreviousClick,
                    leadingIcon = painterResource(R.drawable.arrow_left_alt)
                )
                Spacer(Modifier.weight(1f))
                NavigationButton(
                    text = TextOrResource.Resource(R.string.quiz_btn_next),
                    enabled = state.canGoNext,
                    onClick = onNextClick,
                    trailingIcon = painterResource(R.drawable.arrow_right_alt)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = FIGMA_MEDIUM_PADDING),
                verticalAlignment = Alignment.Top

            ) {
                Icon(
                    painter = painterResource(R.drawable.ellipse_icon),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 4.dp),
                    tint = Theme.colors.purple800
                )
                Text(
                    text = state.questionText,
                    modifier = Modifier
                        .padding(start = FIGMA_LOW_PADDING, end = 12.dp)
                        .weight(1f),
                    style = Theme.typography.body3Strong
                )
                FilledIconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(FIGMA_RADIUS),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Theme.colors.black10
                    )
                ) {
                    Icon(
                        painter = favoriteIcon,
                        contentDescription = null,
                        modifier = Modifier.padding(12.dp),
                        tint = if (isFavorite) {
                            Theme.colors.red700
                        } else {
                            Theme.colors.black600
                        }
                    )
                }
            }
            Text(
                text = if (state.isAnswerVisible) {
                    stringResource(R.string.quiz_collapse_answer)
                } else {
                    stringResource(R.string.quiz_show_answer)
                },
                modifier = Modifier
                    .padding(top = FIGMA_MEDIUM_PADDING, start = 12.dp)
                    .clickable(onClick = onShowAnswerClick),
                style = Theme.typography.body2,
                color = Theme.colors.purple700
            )
            if (state.isAnswerVisible) {
                Text(
                    text = state.shortAnswer,
                    modifier = Modifier.padding(top = 12.dp),
                    style = Theme.typography.body3
                )
            }
            Spacer(Modifier.height(FIGMA_VERTICAL_FIRST_AND_LAST_ELEMENT_PADDING))

            Row(Modifier.padding(bottom = FIGMA_MEDIUM_PADDING)) {
                QuizAnswerButton(
                    painter = painterResource(R.drawable.thumbs_down_icon),
                    text = TextOrResource.Resource(R.string.quiz_answer_unknown),
                    onClick = onUnknownClick,
                    isSelected = state.currentAnswer == InterviewQuizState.Loaded.QuizAnswer.UNKNOWN
                )
                Spacer(Modifier.weight(1f))
                QuizAnswerButton(
                    painter = painterResource(R.drawable.thumbs_up_icon),
                    text = TextOrResource.Resource(R.string.quiz_answer_known),
                    onClick = onKnownClick,
                    isSelected = state.currentAnswer == InterviewQuizState.Loaded.QuizAnswer.KNOWN
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(bottom = FIGMA_MEDIUM_PADDING),
                color = Theme.colors.black100
            )
            if (state.isLastQuestion) {
                PrimaryButton(
                    onClick = onResultClick,
                    modifier = Modifier.height(48.dp),

                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.quiz_check_result),
                            style = Theme.typography.body3Strong
                        )
                    }
                }
            } else {
                SecondaryButton(
                    onClick = {},
                    modifier = Modifier
                        .width(170.dp)
                        .height(48.dp)
                        .align(Alignment.End),
                    colors = YeahubButtonDefaults.secondaryButtonColors(
                        containerColor = Theme.colors.red100,
                        contentColor = Theme.colors.red700
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.quiz_btn_complete),
                            style = Theme.typography.body3Strong
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DefaultCard(
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white900),
        shape = RoundedCornerShape(FIGMA_RADIUS),
        elevation = CardDefaults.cardElevation(FIGMA_CARD_ELEVATION),
        content = content
    )
}

@Composable
private fun NavigationButton(
    text: TextOrResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    contentPadding: PaddingValues = PaddingValues()
) {

    val context = LocalContext.current
    val color = if (enabled) Theme.colors.purple700 else Theme.colors.purple300

    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding
        ) {
        if (leadingIcon != null) {
            Icon(
                painter = leadingIcon,
                contentDescription = null,
                tint = color
            )
            Spacer(Modifier.width(FIGMA_LOW_PADDING))
        }
        Text(
            text = when (text) {
                is TextOrResource.Resource -> text.getString(context)
                is TextOrResource.Text -> text.text
            },
            color = color,
            style = Theme.typography.body3Strong
        )
        if (trailingIcon != null) {
            Spacer(Modifier.width(FIGMA_LOW_PADDING))
            Icon(
                painter = trailingIcon,
                contentDescription = null,
                tint = color
            )
        }
    }
}

@Composable
private fun QuizAnswerButton(
    painter: Painter,
    text: TextOrResource,
    onClick: () -> Unit,
    isSelected: Boolean
) {

    val context = LocalContext.current

    val contentColor = if (isSelected) {
        Theme.colors.purple700
    } else {
        Theme.colors.black700
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .width(120.dp)
            .height(48.dp),
        shape = RoundedCornerShape(FIGMA_RADIUS),
        colors = ButtonDefaults.buttonColors(
            containerColor = Theme.colors.black10,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(
            horizontal = 12.dp,
            vertical = FIGMA_LOW_PADDING
        )
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.padding(end = FIGMA_LOW_PADDING)
        )
        Text(
            text = when (text) {
                is TextOrResource.Text -> text.text
                is TextOrResource.Resource -> text.getString(context)
            },
            style = Theme.typography.body2
        )
    }
}

private val questions = listOf(
    InterviewQuizState.Loaded.VoQuestion(
        id = 0,
        title = "Что такое Virtual DOM, и как он работает?",
        shortAnswer = "Виртуальный DOM (VDOM) — это легковесное представление реального DOM в памяти, которое используется в JavaScript-библиотеках, таких как React и Vue, для повышения производительности веб-приложений."
    ),
    InterviewQuizState.Loaded.VoQuestion(
        id = 1,
        title = "Что такое Virtual DOM, и как он работает?",
        shortAnswer = "Виртуальный DOM (VDOM) — это легковесное представление реального DOM в памяти, которое используется в JavaScript-библиотеках, таких как React и Vue, для повышения производительности веб-приложений."
    ),
    InterviewQuizState.Loaded.VoQuestion(
        id = 2,
        title = "Что такое Virtual DOM, и как он работает?",
        shortAnswer = "Виртуальный DOM (VDOM) — это легковесное представление реального DOM в памяти, которое используется в JavaScript-библиотеках, таких как React и Vue, для повышения производительности веб-приложений."
    ),
    InterviewQuizState.Loaded.VoQuestion(
        id = 3,
        title = "Что такое Virtual DOM, и как он работает?",
        shortAnswer = "Виртуальный DOM (VDOM) — это легковесное представление реального DOM в памяти, которое используется в JavaScript-библиотеках, таких как React и Vue, для повышения производительности веб-приложений."
    )
)

private val answers = mapOf(
    0.toLong() to InterviewQuizState.Loaded.QuizAnswer.KNOWN,
    1.toLong() to InterviewQuizState.Loaded.QuizAnswer.UNKNOWN,
    2.toLong() to InterviewQuizState.Loaded.QuizAnswer.NONE,
    3.toLong() to InterviewQuizState.Loaded.QuizAnswer.UNKNOWN,
)

class QuizScreenStateParamProvider : PreviewParameterProvider<InterviewQuizState> {
    override val values: Sequence<InterviewQuizState> = sequenceOf(
        InterviewQuizState.Loaded(
            questions = questions,
            questionsCount = questions.count(),
            currentQuestionIndex = 1,
            isAnswerVisible = false,
            answers = answers
        ),
        InterviewQuizState.Loaded(
            questions = questions,
            questionsCount = questions.count(),
            currentQuestionIndex = 0,
            isAnswerVisible = true,
            answers = answers
        ),
        InterviewQuizState.Loaded(
            questions = questions,
            questionsCount = questions.count(),
            currentQuestionIndex = 3,
            isAnswerVisible = true,
            answers = answers
        ),
        InterviewQuizState.Loading,
        InterviewQuizState.Error(
            Throwable("Не удалось загрузить данные")
        )
    )
}

@StaticPreview
@Composable
fun InterviewQuizScreen(
    @PreviewParameter(QuizScreenStateParamProvider::class)
    state: InterviewQuizState
) {
    ScreenUI(
        headerText = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text),
        state = state,
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DynamicPreviewUI() {

    val mockViewModel = viewModelCreator<InterviewQuizViewModel> {
        InterviewQuizViewModel(InterviewQuizScreenMapper)
    }

    val state by mockViewModel.screenState.collectAsState()

    ScreenUI(
        headerText = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text),
        state = state,
        onEvent = mockViewModel::onEvent
    )
}

typealias ViewModelCreator = () -> ViewModel?

class ViewModelFactory(
    private val viewModelCreator: ViewModelCreator = { null },
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModelCreator() as T
}

@Composable
inline fun <reified VM : ViewModel> viewModelCreator(noinline creator: ViewModelCreator): VM =
    viewModel(factory = remember { ViewModelFactory(creator) })