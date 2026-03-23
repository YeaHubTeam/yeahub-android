package ru.yeahub.interview_trainer.impl.interviewQuizResult.ui

import InterviewQuizResultState
import InterviewQuizResultViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.core_ui.component.ErrorScreen
import ru.yeahub.core_ui.component.KnownAnswerButton
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_ui.component.UnknownAnswerButton
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Colors
import ru.yeahub.core_ui.theme.Theme.typography
import ru.yeahub.core_ui.theme.Typography
import ru.yeahub.core_ui.theme.colors
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.interview_trainer.impl.R
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.InterviewQuizState
import ru.yeahub.interview_trainer.impl.interviewQuizResult.InterviewQuizResultEvent

private val H_PADDING = 16.dp
private val V_BLOCK = 16.dp
private val V_SECTION = 32.dp
private val V_TINY = 8.dp
private val CARD_RADIUS = 12.dp
private val CARD_ELEVATION = 4.dp
private val SKILL_BAR_HEIGHT = 12.dp
private val SKILL_BAR_SPACING = 12.dp

@Composable
fun InterviewQuizResultScreen(titleTopAppBarResId: Int) {
    val viewModel: InterviewQuizResultViewModel = koinViewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()

    ScreenUI(
        state = state,
        onEvent = viewModel::onEvent,
        titleTopAppBar = TextOrResource.Resource(titleTopAppBarResId)
    )
}

@Composable
private fun ScreenUI(
    state: State<InterviewQuizResultState>,
    onEvent: (InterviewQuizResultEvent) -> Unit,
    titleTopAppBar: TextOrResource,
) {
    Scaffold(
        containerColor = colors.black10,
        topBar = {
            TopAppBarWithBottomBorder(
                title = TextOrResource.Resource(R.string.create_quiz_top_bar_header_text),
                onBackClick = { onEvent(InterviewQuizResultEvent.OnBackClick) }
            )
        }
    ) { innerPadding ->
        when (val currentState = state.value) {
            is InterviewQuizResultState.Loading -> {
                InterviewQuizResultLoadingContent()
            }

            is InterviewQuizResultState.Error -> {
                ErrorScreen(
                    error = currentState.throwable.localizedMessage,
                    errorText = TextOrResource.Resource(R.string.error_screen_text),
                    titleText = TextOrResource.Resource(R.string.title_error_screen_text),
                    backText = TextOrResource.Resource(R.string.back_error_screen_text),
                    unknownErrorText = TextOrResource.Resource(R.string.unknown_error_screen_text),
                    onBack = {}
                )
            }
            is InterviewQuizResultState.Loaded -> {
                BaseInterviewQuizResultScreen(
                    state = currentState,
                    innerPadding = innerPadding,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun BaseInterviewQuizResultScreen(
    state: InterviewQuizResultState.Loaded,
    innerPadding: PaddingValues,
    onEvent: (InterviewQuizResultEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState)
            .padding(horizontal = H_PADDING),
        verticalArrangement = Arrangement.spacedBy(V_SECTION)
    ) {
        TitleSection(
            typography = typography,
            colors = colors
        )
        Card(
            shape = RoundedCornerShape(CARD_RADIUS),
            elevation = CardDefaults.cardElevation(CARD_ELEVATION),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(H_PADDING)) {
                Text(
                    text = stringResource(R.string.interview_quiz_result_statistics),
                    style = typography.body3Accent.copy(fontWeight = FontWeight.Bold),
                    color = colors.black900
                )
                Spacer(Modifier.height(V_BLOCK))
                OverallProgressStatistics(
                    percentage = state.overallPercentage.coerceIn(0f, 1f),
                    totalQuestions = state.totalQuestions,
                    newQuestions = state.newQuestions,
                    inProgress = state.inProgress,
                    studied = state.studied,
                    colors = colors,
                    typography = typography
                )

                Spacer(Modifier.height(32.dp))

                Text(
                    text = stringResource(R.string.interview_quiz_result_progress),
                    style = typography.head4,
                    color = colors.black900
                )
                Spacer(Modifier.height(V_BLOCK))

                state.skills.forEach { skill ->
                    SkillProgressRow(skill, colors, typography)
                }

                Spacer(Modifier.height(V_TINY))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(R.string.interview_quiz_result_show_all),
                        style = typography.body3,
                        color = colors.purple700,
                        modifier = Modifier.clickable {
                            onEvent(InterviewQuizResultEvent.ViewDetailedStats)
                        }
                    )
                }
            }
        }
        Card(
            shape = RoundedCornerShape(CARD_RADIUS),
            elevation = CardDefaults.cardElevation(CARD_ELEVATION),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(H_PADDING)
            ) {
                Text(
                    text = stringResource(R.string.interview_quiz_result_questuions),
                    style = typography.body4,
                    color = colors.black900,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                state.questions.forEach { question ->
                    QuestionItem(question, colors, typography)
                }
            }
        }
        Spacer(Modifier.height(V_SECTION))
    }
}

@Composable
private fun InterviewQuizResultLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun TitleSection(
    typography: Typography,
    colors: Colors
) {
    Text(
        text = stringResource(R.string.interview_quiz_result_result),
        style = typography.head5,
        color = colors.black900,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
private fun SkillProgressRow(
    skill: InterviewQuizResultState.Loaded.VoSkillStat,
    colors: Colors,
    typography: Typography
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = skill.name,
            style = typography.body3Strong,
            color = colors.black900,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = { skill.current.toFloat() / skill.max },
                modifier = Modifier
                    .weight(1f)
                    .height(SKILL_BAR_HEIGHT)
                    .clip(RoundedCornerShape(4.dp)),
                color = colors.purple700,
                trackColor = colors.purple400,
                strokeCap = StrokeCap.Round,
                gapSize = (-10).dp,
                drawStopIndicator = {}
            )

            Spacer(Modifier.width(SKILL_BAR_SPACING))

            Text(
                text = "${skill.current}/${skill.max}".trim(),
                style = typography.body3Strong,
                color = colors.black900,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    colors: Colors,
    typography: Typography
) {
    Card(
        modifier = Modifier
            .widthIn(min = 72.dp)
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = typography.body2,
                color = colors.black400,
                maxLines = 1
            )

            Text(
                text = value,
                style = typography.body3Strong,
                color = colors.black800
            )
        }
    }
}

@Composable
private fun OverallProgressStatistics(
    percentage: Float,
    totalQuestions: Int,
    newQuestions: Int,
    inProgress: Int,
    studied: Int,
    colors: Colors,
    typography: Typography
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier.size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = colors.yellow600.copy(alpha = 0.15f),
                        shape = CircleShape
                    )
            )

            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = colors.yellow600.copy(alpha = 0.5f),
                strokeWidth = 24.dp,
                strokeCap = StrokeCap.Butt,
                trackColor = Color.Transparent
            )

            CircularProgressIndicator(
                progress = { percentage },
                modifier = Modifier.fillMaxSize(),
                color = colors.green800,
                strokeWidth = 24.dp,
                strokeCap = StrokeCap.Round,
                trackColor = Color.Transparent
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${(percentage * 100).toInt()}%",
                    style = typography.head4.copy(fontWeight = FontWeight.Bold),
                    color = colors.black700
                )
                Text(
                    text = stringResource(R.string.interview_quiz_result_done),
                    style = typography.body4,
                    color = colors.black700
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatItem(stringResource(R.string.interview_quiz_result_overall), totalQuestions.toString(), colors, typography)
            StatItem(stringResource(R.string.interview_quiz_result_new), newQuestions.toString(), colors, typography)
            StatItem(stringResource(R.string.interview_quiz_result_in_progress), inProgress.toString(), colors, typography)
            StatItem(stringResource(R.string.interview_quiz_result_studied), studied.toString(), colors, typography)
        }
    }
}

@Composable
private fun QuestionItem(
    question: InterviewQuizResultState.Loaded.VoQuestionResult,
    colors: Colors,
    typography: Typography
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(CARD_RADIUS),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(H_PADDING),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F1C2E))
            ) {
                Image(
                    painter = painterResource(id = ru.yeahub.ui.R.drawable.question_image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = question.questionText,
                    style = typography.body3Strong,
                    color = colors.black900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF3F3F3))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isCorrect = question.isCorrect

                    if (isCorrect) {
                        KnownAnswerButton(
                            enabled = false,
                            isHighlighted = true,
                            onClick = {}
                        )
                    } else {
                        UnknownAnswerButton(
                            enabled = false,
                            isHighlighted = false,
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}

@StaticPreview
@Composable
internal fun InterviewQuizResultScreenPreview(
    @PreviewParameter(InterviewQuizResultStateParamProvider::class)
    state: InterviewQuizResultState,
) {
    ScreenUI(
        state = rememberUpdatedState(state),
        onEvent = {},
        titleTopAppBar = TextOrResource.Resource(R.string.interview_quiz_result_result)
    )
}

class InterviewQuizResultStateParamProvider :
    PreviewParameterProvider<InterviewQuizResultState> {

    override val values = sequenceOf(
        InterviewQuizResultState.Loaded(
            overallPercentage = 0.75f,
            totalQuestions = 20,
            newQuestions = 50,
            inProgress = 120,
            studied = 12,
            skills = listOf(
                InterviewQuizResultState.Loaded.VoSkillStat("HTML", 60, 120),
                InterviewQuizResultState.Loaded.VoSkillStat("CSS", 60, 120),
                InterviewQuizResultState.Loaded.VoSkillStat("JavaScript", 60, 120),
                InterviewQuizResultState.Loaded.VoSkillStat("React", 60, 120),
                InterviewQuizResultState.Loaded.VoSkillStat("PHP", 60, 120),
                InterviewQuizResultState.Loaded.VoSkillStat("JavaScript", 60, 120)
            ),
            questions = listOf(
                InterviewQuizResultState.Loaded.VoQuestionResult(
                    "Что такое Virtual DOM, и как он работает?",
                    false
                ),
                InterviewQuizResultState.Loaded.VoQuestionResult(
                    "Что такое Virtual DOM, и как он работает?",
                    true
                ),
                InterviewQuizResultState.Loaded.VoQuestionResult(
                    "Что такое Virtual DOM, и как он работает?",
                    false
                ),
                InterviewQuizResultState.Loaded.VoQuestionResult(
                    "Что такое Virtual DOM, и как он работает?",
                    true
                ),
                InterviewQuizResultState.Loaded.VoQuestionResult(
                    "Что такое Virtual DOM, и как он работает?",
                    false
                )
            )
        )
    )
}