package ru.yeahub.home.impl.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.home.impl.R
import ru.yeahub.home.impl.presentation.component.CollectionCard
import ru.yeahub.home.impl.presentation.component.InterviewHistoryCard
import ru.yeahub.home.impl.presentation.component.InterviewProgressChart
import ru.yeahub.home.impl.presentation.component.QuestionCard
import ru.yeahub.home.impl.presentation.component.TextArrowAction
import ru.yeahub.home.impl.presentation.demo.HomeDemoData
import ru.yeahub.home.impl.presentation.model.CollectionUiModel
import ru.yeahub.home.impl.presentation.model.InterviewHistoryUiModel
import ru.yeahub.home.impl.presentation.model.InterviewStatsUiModel
import ru.yeahub.home.impl.presentation.model.InterviewTrainingUiModel
import ru.yeahub.home.impl.presentation.model.QuestionUiModel
import ru.yeahub.home.impl.presentation.state.HomeScreenState
import ru.yeahub.home.impl.presentation.theme.HomeTypography

private const val MAX_VISIBLE_QUESTIONS = 3
private const val MAX_VISIBLE_INTERVIEWS = 4
private const val STATS_CARD_SHADOW_ARGB = 0x1A6A6376

@Composable
fun HomeScreen(state: HomeScreenState.Content, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.colors.black10),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            HomeHeader()
        }

        item {
            InterviewTrainingCard(training = state.training)
            Spacer(Modifier.height(2.dp))
        }

        item {
            InterviewStatisticsCard(stats = state.interviewStats)
        }

        item {
            QuestionsCard(questions = state.questions)
        }

        item {
            HistoryCard(
                interviews = state.interviewHistory,
            )
        }

        item {
            CollectionsCard(
                collections = state.collections,
            )
        }
    }
}

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Главная",
            style = Theme.typography.head5,
            color = Theme.colors.black900
        )

        Image(
            painter = painterResource(R.drawable.avatar_placeholder),
            contentDescription = "Профиль",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    //Переход на экран профиля
                }
        )
    }
}

@Composable
fun InterviewTrainingCard(
    training: InterviewTrainingUiModel,
    modifier: Modifier = Modifier,
    onStartInterview: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Theme.colors.mainShadow,
                ambientColor = Theme.colors.mainShadow
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colors.white900
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 14.dp)
        ) {
            Text(
                text = "Тренажёр собеседований",
                style = HomeTypography.sectionTitle,
                color = Theme.colors.black900
            )

            Spacer(Modifier.height(16.dp))

            InterviewProgress(training.currentQuestionNumber, training.totalQuestions)

            Spacer(Modifier.height(12.dp))

            Text(
                text = training.questionText,
                style = HomeTypography.questionTitleLarge,
                color = Theme.colors.black900
            )

            Spacer(Modifier.height(16.dp))

            TextArrowAction(
                "Пройти собеседование",
                onStartInterview,
                modifier = Modifier.align(Alignment.End),
                contentPadding = PaddingValues(0.dp),
                verticalOffset = (0).dp
            )
        }
    }
}

@Composable
private fun InterviewProgress(
    currentQuestion: Int,
    totalQuestions: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (totalQuestions > 0) {
        (currentQuestion.toFloat() / totalQuestions).coerceIn(0f, 1f)
    } else {
        0f
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .background(Theme.colors.purple300)
                .progressSemantics(progress)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .background(
                        color = Theme.colors.purple700,
                        shape = CircleShape
                    )
            )
        }

        Text(
            text = "Вопрос викторины $currentQuestion из $totalQuestions",
            style = HomeTypography.questionProgressText,
            color = Theme.colors.black500
        )
    }
}

@Composable
fun InterviewStatisticsCard(
    modifier: Modifier = Modifier,
    stats: InterviewStatsUiModel,
    onStartInterview: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Theme.colors.mainShadow,
                ambientColor = Theme.colors.mainShadow
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colors.white900
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Статистика собеседований",
                style = HomeTypography.sectionTitle,
                color = Theme.colors.black900
            )

            Spacer(Modifier.height(16.dp))

            InterviewProgressChart(
                progress = stats.chartPercent,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(12.dp))

            StatisticsMiniCards(
                total = stats.total,
                new = stats.new,
                inProgress = stats.inProcess,
                studied = stats.studied
            )

            Spacer(Modifier.height(16.dp))

            TextArrowAction(
                "Пройти собеседование",
                onStartInterview,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun StatisticsMiniCards(
    modifier: Modifier = Modifier,
    total: Int,
    new: Int,
    inProgress: Int,
    studied: Int
) {
    val items = listOf(
        "Всего" to total,
        "Новые" to new,
        "В процессе" to inProgress,
        "Изучено" to studied
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        items.forEach { (label, value) ->
            Card(
                modifier = Modifier
                    .widthIn(min = 60.dp)
                    .height(48.dp)
                    .dropShadow(
                        shape = RoundedCornerShape(8.dp),
                        shadow = Shadow(
                            radius = 10.dp,
                            color = Color(STATS_CARD_SHADOW_ARGB),
                            offset = DpOffset(0.dp, 4.dp)
                        )
                    ),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Theme.colors.white900),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Text(
                        text = label,
                        style = HomeTypography.statsMiniCardLabel,
                        color = Theme.colors.black400
                    )

                    Text(
                        text = value.toString(),
                        modifier = Modifier.offset(x = (-0.5).dp, y = 3.5.dp),
                        style = HomeTypography.statsMiniCardLabel.copy(
                            fontWeight = FontWeight.Normal
                        ),
                        color = Theme.colors.black800
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionsCard(
    modifier: Modifier = Modifier,
    questions: List<QuestionUiModel>,
    onStartStudy: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Theme.colors.mainShadow,
                ambientColor = Theme.colors.mainShadow
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colors.white900
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Список вопросов",
                modifier = Modifier.offset(y = 1.dp),
                style = HomeTypography.sectionTitle,
                color = Theme.colors.black900
            )

            Spacer(Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(9.7.dp)) {
                questions.take(MAX_VISIBLE_QUESTIONS).forEach { question ->
                    QuestionCard(question = question)
                }
            }

            Spacer(Modifier.height(16.dp))

            TextArrowAction(
                "Изучить",
                onStartStudy,
                modifier = Modifier.align(Alignment.End),
                verticalOffset = 0.dp
            )
        }
    }
}

@Composable
fun HistoryCard(
    modifier: Modifier = Modifier,
    interviews: List<InterviewHistoryUiModel>,
    onShowHistory: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Theme.colors.mainShadow,
                ambientColor = Theme.colors.mainShadow
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white900)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(
                text = "История собеседований",
                style = HomeTypography.sectionTitle,
                color = Theme.colors.black900
            )

            Spacer(Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                interviews.take(MAX_VISIBLE_INTERVIEWS).forEach { interview ->
                    InterviewHistoryCard(interview = interview)
                }
            }

            Spacer(Modifier.height(16.dp))

            TextArrowAction(
                "Подробнее",
                onShowHistory,
                modifier = Modifier.align(Alignment.End),
                verticalOffset = (-0.5).dp
            )
        }
    }
}

@Composable
fun CollectionsCard(
    modifier: Modifier = Modifier,
    collections: List<CollectionUiModel>,
    onShowCollections: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Theme.colors.mainShadow,
                ambientColor = Theme.colors.mainShadow
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white900)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Коллекции",
                style = HomeTypography.sectionTitle.copy(
                    letterSpacing = 0.20.sp
                ),
                color = Theme.colors.black900
            )

            Spacer(Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                collections.take(2).forEach { collection ->
                    CollectionCard(collection = collection)
                }
            }

            Spacer(Modifier.height(16.dp))

            TextArrowAction(
                "Подробнее",
                onShowCollections,
                modifier = Modifier.align(Alignment.End),
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 3000
)
@Composable
fun HomeScreenPreview() {
    YeaHubTheme {
        HomeScreen(state = HomeDemoData.content)
    }
}
