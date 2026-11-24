package ru.yeahub.main.presentation.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.yeahub.R
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme

private const val PERCENT_DIVISOR = 100f
private const val QUESTION_PREVIEW_COUNT = 3
private const val INTERVIEW_HISTORY_ITEM_COUNT = 4
private const val INTERVIEW_PROGRESS_TRACK_COLOR_VALUE = 0xFFFFE7AE
private const val INTERVIEW_PROGRESS_CENTER_COLOR_VALUE = 0xFFFFFAEC
private const val INNER_CARD_SHADOW_COLOR_VALUE = 0x406A6376
private const val COLLECTION_TAG_TEXT_COLOR_VALUE = 0xFF008616

private val interviewProgressTrackColor = Color(INTERVIEW_PROGRESS_TRACK_COLOR_VALUE)
private val interviewProgressCenterColor = Color(INTERVIEW_PROGRESS_CENTER_COLOR_VALUE)
private val innerCardShadowColor = Color(INNER_CARD_SHADOW_COLOR_VALUE)
private val collectionTagTextColor = Color(COLLECTION_TAG_TEXT_COLOR_VALUE)

private val questionMetricLabelStyle = TextStyle(
    fontSize = 12.sp,
    lineHeight = 14.4.sp,
    fontFamily = FontFamily(
        Font(ru.yeahub.ui.R.font.sf_pro_text_regular),
    ),
    fontWeight = FontWeight.Normal,
)

private val mainActionTextStyle = TextStyle(
    fontSize = 16.sp,
    lineHeight = 24.sp,
    fontFamily = FontFamily(
        Font(ru.yeahub.ui.R.font.sf_pro_text_regular),
    ),
    fontWeight = FontWeight.SemiBold,
)

private val sectionHeadingTextStyle = TextStyle(
    fontSize = 18.sp,
    lineHeight = 21.6.sp,
    fontFamily = FontFamily(
        Font(ru.yeahub.ui.R.font.sf_pro_display_medium),
    ),
    fontWeight = FontWeight.Medium,
)

private val historyDateTextStyle = TextStyle(
    fontSize = 14.sp,
    lineHeight = 16.8.sp,
    fontFamily = FontFamily(
        Font(ru.yeahub.ui.R.font.sf_pro_text_regular),
    ),
    fontWeight = FontWeight.Normal,
)

private val historyTitleTextStyle = TextStyle(
    fontSize = 16.sp,
    lineHeight = 20.8.sp,
    fontFamily = FontFamily(
        Font(ru.yeahub.ui.R.font.sf_pro_text_regular),
    ),
    fontWeight = FontWeight.SemiBold,
)

private val collectionTagTextStyle = TextStyle(
    fontSize = 12.sp,
    lineHeight = 14.4.sp,
    fontFamily = FontFamily(
        Font(ru.yeahub.ui.R.font.sf_pro_text_regular),
    ),
    fontWeight = FontWeight.SemiBold,
)

private val collectionTitleTextStyle = TextStyle(
    fontSize = 16.sp,
    lineHeight = 20.8.sp,
    fontFamily = FontFamily(
        Font(ru.yeahub.ui.R.font.sf_pro_text_regular),
    ),
    fontWeight = FontWeight.Medium,
)

private val collectionInfoTextStyle = TextStyle(
    fontSize = 14.sp,
    lineHeight = 16.8.sp,
    fontFamily = FontFamily(
        Font(ru.yeahub.ui.R.font.sf_pro_text_regular),
    ),
    fontWeight = FontWeight.Normal,
)

private val collectionCategoryTextStyle = TextStyle(
    fontSize = 16.sp,
    lineHeight = 20.8.sp,
    fontFamily = FontFamily(
        Font(ru.yeahub.ui.R.font.sf_pro_text_regular),
    ),
    fontWeight = FontWeight.Normal,
)

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.colors.black10)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 16.dp,
                top = 29.dp,
                end = 16.dp,
                bottom = 29.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        MainHeader()

        InterviewTrainerCard(
            currentQuestion = 10,
            questionsCount = 45,
            question = "Что такое Virtual DOM, и как он работает?",
            onInterviewClick = {},
        )

        InterviewStatisticsCard(
            progress = 75,
            total = 20,
            new = 50,
            inProgress = 120,
            learned = 12,
            onInterviewClick = {},
        )

        QuestionListCard(
            onLearnClick = {},
        )

        InterviewHistoryCard(
            onMoreClick = {},
        )

        CollectionsCard(
            onMoreClick = {},
        )
    }
}

@Composable
private fun MainHeader(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Главная",
            modifier = Modifier.weight(1f),
            style = Theme.typography.head5,
            color = Theme.colors.black900,
            maxLines = 1,
        )

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.avatar_off),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Composable
private fun InterviewTrainerCard(
    currentQuestion: Int,
    questionsCount: Int,
    question: String,
    onInterviewClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress = if (questionsCount == 0) {
        0f
    } else {
        currentQuestion.toFloat() / questionsCount
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(176.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Theme.colors.mainShadow,
                spotColor = Theme.colors.mainShadow,
            )
            .background(
                color = Theme.colors.white900,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Тренажёр собеседований",
            style = sectionHeadingTextStyle,
            color = Theme.colors.black900,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(12.dp))

        InterviewProgressBar(progress = progress)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Вопрос викторины $currentQuestion из $questionsCount",
            style = Theme.typography.body2Accent,
            color = Theme.colors.black500,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = question,
            style = Theme.typography.body3Accent,
            color = Theme.colors.black900,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.weight(1f))

        MainActionButton(
            text = "Пройти собеседование",
            onClick = onInterviewClick,
            modifier = Modifier.align(Alignment.End),
        )
    }
}

@Composable
private fun InterviewProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Theme.colors.purple300),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Theme.colors.purple700),
        )
    }
}

@Composable
private fun MainActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = mainActionTextStyle,
            color = Theme.colors.purple700,
            maxLines = 1,
        )

        Icon(
            painter = painterResource(R.drawable.arrow_right),
            contentDescription = null,
            modifier = Modifier.offset(y = 1.dp),
            tint = Theme.colors.purple700,
        )
    }
}

@Composable
private fun InterviewStatisticsCard(
    progress: Int,
    total: Int,
    new: Int,
    inProgress: Int,
    learned: Int,
    onInterviewClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(411.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Theme.colors.mainShadow,
                spotColor = Theme.colors.mainShadow,
            )
            .background(
                color = Theme.colors.white900,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Статистика собеседований",
            modifier = Modifier.fillMaxWidth(),
            style = sectionHeadingTextStyle,
            color = Theme.colors.black900,
            maxLines = 1,
        )

        InterviewCircularProgress(
            progress = progress,
        )

        InterviewStatistics(
            total = total,
            new = new,
            inProgress = inProgress,
            learned = learned,
        )

        MainActionButton(
            text = "Пройти собеседование",
            onClick = onInterviewClick,
            modifier = Modifier.align(Alignment.End),
        )
    }
}

@Composable
private fun InterviewCircularProgress(
    progress: Int,
    modifier: Modifier = Modifier,
) {
    val normalizedProgress = (progress / PERCENT_DIVISOR).coerceIn(0f, 1f)
    val trackColor = interviewProgressTrackColor
    val centerColor = interviewProgressCenterColor
    val progressColor = Theme.colors.green800

    Box(
        modifier = modifier.size(241.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            val strokeWidth = 16.dp.toPx()

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                ),
            )

            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * normalizedProgress,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                ),
            )
        }

        Box(
            modifier = Modifier
                .size(209.dp)
                .background(
                    color = centerColor,
                    shape = CircleShape,
                ),
        )

        Text(
            text = "$progress%\nпройдено",
            style = Theme.typography.body4,
            color = Theme.colors.black700,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun InterviewStatistics(
    total: Int,
    new: Int,
    inProgress: Int,
    learned: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        InterviewStatisticItem(
            title = "Всего",
            value = total,
        )

        InterviewStatisticItem(
            title = "Новые",
            value = new,
        )

        InterviewStatisticItem(
            title = "В процессе",
            value = inProgress,
        )

        InterviewStatisticItem(
            title = "Изучено",
            value = learned,
        )
    }
}

@Composable
private fun InterviewStatisticItem(
    title: String,
    value: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .widthIn(min = 60.dp)
            .height(48.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = innerCardShadowColor,
                spotColor = innerCardShadowColor,
            )
            .background(
                color = Theme.colors.white900,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(
                horizontal = 8.dp,
                vertical = 7.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = title,
            style = Theme.typography.body1,
            color = Theme.colors.black400,
            maxLines = 1,
        )

        Text(
            text = value.toString(),
            style = Theme.typography.body1,
            color = Theme.colors.black700,
            maxLines = 1,
        )
    }
}

@Composable
private fun QuestionListCard(
    onLearnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(377.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Theme.colors.mainShadow,
                spotColor = Theme.colors.mainShadow,
            )
            .background(
                color = Theme.colors.white900,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Список вопросов",
            style = sectionHeadingTextStyle,
            color = Theme.colors.black900,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            repeat(QUESTION_PREVIEW_COUNT) {
                QuestionPreviewCard(
                    question = "Что такое Virtual DOM, и как он работает?",
                    rating = 4,
                    difficulty = 10,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        MainActionButton(
            text = "Изучить",
            onClick = onLearnClick,
            modifier = Modifier.align(Alignment.End),
        )
    }
}

@Composable
private fun QuestionPreviewCard(
    question: String,
    rating: Int,
    difficulty: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(81.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = innerCardShadowColor,
                spotColor = innerCardShadowColor,
            )
            .background(
                color = Theme.colors.white900,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(
                start = 12.dp,
                top = 8.dp,
                end = 12.dp,
                bottom = 12.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = question,
            style = Theme.typography.body3,
            color = Theme.colors.black900,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            QuestionMetric(
                title = "Рейтинг:",
                value = rating,
                modifier = Modifier.width(106.dp),
            )

            QuestionMetric(
                title = "Сложность:",
                value = difficulty,
                modifier = Modifier.width(122.dp),
            )
        }
    }
}

@Composable
private fun QuestionMetric(
    title: String,
    value: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(32.dp)
            .background(
                color = Theme.colors.black10,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(
                start = 12.dp,
                top = 4.dp,
                end = 12.dp,
                bottom = 4.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = questionMetricLabelStyle,
            color = Theme.colors.black800,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .requiredSize(24.dp)
                .background(
                    color = Theme.colors.purple700,
                    shape = RoundedCornerShape(4.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = value.toString(),
                style = questionMetricLabelStyle,
                color = Theme.colors.white900,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun InterviewHistoryCard(
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(554.dp)
            .background(
                color = Theme.colors.white900,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.End,
    ) {
        Text(
            text = "История собеседований",
            modifier = Modifier.fillMaxWidth(),
            style = sectionHeadingTextStyle,
            color = Theme.colors.black900,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            repeat(INTERVIEW_HISTORY_ITEM_COUNT) { index ->
                InterviewHistoryItem(
                    date = "15/04/2024",
                    interviewNumber = index + 1,
                    correctAnswers = 45,
                    incorrectAnswers = 15,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        MainActionButton(
            text = "Подробнее",
            onClick = onMoreClick,
        )
    }
}

@Composable
private fun InterviewHistoryItem(
    date: String,
    interviewNumber: Int,
    correctAnswers: Int,
    incorrectAnswers: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(102.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = innerCardShadowColor,
                spotColor = innerCardShadowColor,
            )
            .background(
                color = Theme.colors.white900,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = date,
            style = historyDateTextStyle,
            color = Theme.colors.black400,
            maxLines = 1,
        )

        Text(
            text = "Собеседование № $interviewNumber",
            style = historyTitleTextStyle,
            color = Theme.colors.black900,
            maxLines = 1,
        )

        InterviewResult(
            correctAnswers = correctAnswers,
            incorrectAnswers = incorrectAnswers,
        )
    }
}

@Composable
private fun InterviewResult(
    correctAnswers: Int,
    incorrectAnswers: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Результат:",
            style = historyDateTextStyle,
            color = Theme.colors.black400,
            maxLines = 1,
        )

        InterviewResultValue(
            icon = R.drawable.up_arrow,
            value = correctAnswers,
        )

        InterviewResultValue(
            icon = R.drawable.down_arrow,
            value = incorrectAnswers,
        )
    }
}

@Composable
private fun InterviewResultValue(
    icon: Int,
    value: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified,
        )

        Text(
            text = value.toString(),
            style = historyDateTextStyle,
            color = Theme.colors.black600,
            maxLines = 1,
        )
    }
}

@Composable
private fun CollectionsCard(
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Theme.colors.white900,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.End,
    ) {
        Text(
            text = "Коллекции",
            modifier = Modifier.fillMaxWidth(),
            style = sectionHeadingTextStyle,
            color = Theme.colors.black900,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(16.dp))

        CollectionPreviewCard()

        Spacer(modifier = Modifier.height(16.dp))

        CollectionPreviewCard()

        Spacer(modifier = Modifier.height(16.dp))

        MainActionButton(
            text = "Подробнее",
            onClick = onMoreClick,
        )
    }
}

@Composable
private fun CollectionPreviewCard(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = innerCardShadowColor,
                spotColor = innerCardShadowColor,
            )
            .background(
                color = Theme.colors.white900,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CollectionImagePlaceholder()

        CollectionTags(
            tags = listOf(
                "Frontend",
                "Mobile",
                "Mobile",
            ),
        )

        Text(
            text = "Что такое Virtual DOM, и как он работает? Митап True Tech Go",
            style = collectionTitleTextStyle,
            color = Theme.colors.black900,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        CollectionInformation()

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Software tester",
                style = collectionCategoryTextStyle,
                color = Theme.colors.black500,
                maxLines = 1,
            )

            Text(
                text = "Fullstack",
                style = collectionCategoryTextStyle,
                color = Theme.colors.black500,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun CollectionImagePlaceholder(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(R.drawable.placeholders),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .height(302.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun CollectionTags(
    tags: List<String>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tags.forEach { tag ->
            Text(
                text = tag,
                modifier = Modifier
                    .background(
                        color = Theme.colors.green007,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(
                        horizontal = 8.dp,
                        vertical = 2.dp,
                    ),
                style = collectionTagTextStyle,
                color = collectionTagTextColor,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun CollectionInformation(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CollectionInformationItem(
            icon = R.drawable.stars_minimalistic,
            text = "Для участников",
        )

        CollectionInformationItem(
            icon = R.drawable.question_square,
            text = "30 вопросов",
        )
    }
}

@Composable
private fun CollectionInformationItem(
    icon: Int,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified,
        )

        Text(
            text = text,
            style = collectionInfoTextStyle,
            color = Theme.colors.purple700,
            maxLines = 1,
        )
    }
}

@Suppress("UnusedPrivateMember")
@StaticPreview
@Composable
private fun MainHeaderPreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.black10)
                .padding(16.dp),
        ) {
            MainHeader()
        }
    }
}

@Suppress("UnusedPrivateMember")
@StaticPreview
@Composable
private fun InterviewTrainerCardPreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.black10)
                .padding(16.dp),
        ) {
            InterviewTrainerCard(
                currentQuestion = 10,
                questionsCount = 45,
                question = "Что такое Virtual DOM, и как он работает?",
                onInterviewClick = {},
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@StaticPreview
@Composable
private fun InterviewStatisticsCardPreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.black10)
                .padding(16.dp),
        ) {
            InterviewStatisticsCard(
                progress = 75,
                total = 20,
                new = 50,
                inProgress = 120,
                learned = 12,
                onInterviewClick = {},
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@StaticPreview
@Composable
private fun QuestionListCardPreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.black10)
                .padding(16.dp),
        ) {
            QuestionListCard(
                onLearnClick = {},
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@StaticPreview
@Composable
private fun InterviewHistoryCardPreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.black10)
                .padding(16.dp),
        ) {
            InterviewHistoryCard(
                onMoreClick = {},
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(
    name = "Collections",
    widthDp = 390,
    heightDp = 1122,
    showBackground = true,
)
@Composable
private fun CollectionsCardPreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.black10)
                .padding(16.dp),
        ) {
            CollectionsCard(
                onMoreClick = {},
            )
        }
    }
}
