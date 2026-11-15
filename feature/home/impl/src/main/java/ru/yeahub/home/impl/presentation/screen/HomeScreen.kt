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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import androidx.compose.ui.tooling.preview.Preview
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.home.impl.R
import ru.yeahub.home.impl.presentation.theme.HomeTypography
import ru.yeahub.home.impl.presentation.component.TextArrowAction


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
        modifier = modifier.fillMaxWidth(),
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
                modifier = Modifier.align(Alignment.End)
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

@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 800
)
@Composable
fun HomeScreenPreview() {
    YeaHubTheme {
        HomeScreen()
    }
}
