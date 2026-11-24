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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.home.impl.R
import ru.yeahub.home.impl.presentation.component.InterviewProgressChart
import ru.yeahub.home.impl.presentation.component.QuestionCard
import ru.yeahub.home.impl.presentation.component.CollectionCard
import ru.yeahub.home.impl.presentation.model.CollectionUiModel
import ru.yeahub.ui.R as CoreUiR
import ru.yeahub.home.impl.presentation.component.InterviewHistoryCard
import ru.yeahub.home.impl.presentation.model.InterviewHistoryUiModel
import ru.yeahub.home.impl.presentation.theme.HomeTypography
import ru.yeahub.home.impl.presentation.component.TextArrowAction
import ru.yeahub.home.impl.presentation.model.InterviewStatsUiModel
import ru.yeahub.home.impl.presentation.model.QuestionUiModel


object PreviewData {
    val collectionData = CollectionUiModel(
        imageRes = CoreUiR.drawable.product_image,
        tags = listOf("Frontend", "Mobile", "Mobile"),
        title = "Что такое Virtual DOM, и как он работает? Митап True Tech Go",
        isForParticipants = true,
        questionsCount = 30,
        additionalTags = listOf("Software tester", "Fullstack")
    )

    val interviewHistory = List(4) { index ->
        InterviewHistoryUiModel(
            interviewDate = "15/04/2024",
            interviewNumber = (index + 1).toString(),
            positiveCount = 45,
            negativeCount = 15
        )
    }

    val interviewStats = InterviewStatsUiModel(
        chartPercent = 0.75f,
        total = 20,
        new = 50,
        inProcess = 120,
        studied = 12
    )

    val questionData = QuestionUiModel(
        question = "Что такое Virtual DOM, и как он работает?",
        rating = 4,
        complexity = 10
    )
}


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
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
            InterviewTrainingCard()
            Spacer(Modifier.height(2.dp))
        }

        item {
            InterviewStatisticsCard(stats = PreviewData.interviewStats)
        }

        item {
            QuestionsCard(questions = List(3) { PreviewData.questionData })
        }

        item {
            HistoryCard(
                interviews = PreviewData.interviewHistory,
            )
        }

        item {
            CollectionsCard(
                collections = List(2) { PreviewData.collectionData },
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

            InterviewProgress(10, 45)

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Что такое Virtual DOM, и как он работает?",
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
                .clip(RoundedCornerShape(50))
                .background(Theme.colors.purple300)
                .progressSemantics(progress)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .background(
                        color = Theme.colors.purple700,
                        shape = RoundedCornerShape(50)
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
                            color = Color(0x1A6A6376),
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
                questions.take(3).forEach { question ->
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
                interviews.take(4).forEach { interview ->
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
        HomeScreen()
    }
}
