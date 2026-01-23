package ru.yeahub.interview_trainer.impl.interviewQuiz.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.interview_trainer.impl.R

@Composable
private fun ScreenUI(
    headerText: TextOrResource
) {

    val currentQuestion = 10
    val questionsCount = 45

    Scaffold(
        containerColor = Theme.colors.black10,
        topBar = {
            TopAppBarWithBottomBorder(
                title = headerText,
                onBackClick = { TODO("onBackClick don't implemented") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp)
        ) {
            QuizProgress(
                currentQuestion,
                questionsCount
            )

            Spacer(Modifier.height(28.dp))

            QuizCard()
        }

    }
}

@Composable
private fun QuizProgress(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier
) {

    val progress = (current.toFloat() / total).coerceIn(0f, 1f)

    Card(
        modifier = modifier.fillMaxWidth().height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colors.white900
        ),
        shape = RoundedCornerShape(16),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(100)),
                color = Theme.colors.purple800,
                trackColor = Theme.colors.purple300,
                gapSize = (-8).dp,
                strokeCap = StrokeCap.Round,
                drawStopIndicator = {}
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = TextOrResource.Text("$current из $total").text,
                color = Theme.colors.black500,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }

}

@Composable
private fun QuizCard(
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colors.white900
        ),
        shape = RoundedCornerShape(16),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Row {
            NavQuizButton(
                text = TextOrResource.Text("Назад"),
                onClick = { TODO() },
                leadingIcon = null
            )
            Spacer(Modifier.weight(1f))
            NavQuizButton(
                text = TextOrResource.Text("Далее"),
                onClick = { TODO() },
                leadingIcon = null
            )
        }
    }
}

@Composable
private fun NavQuizButton(
    text: TextOrResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding
) {

    val context = LocalContext.current

    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding
        ) {
        if (leadingIcon != null) {
            //Icon
        }
        Text(
            text = when (text) {
                is TextOrResource.Resource -> text.getString(context)
                is TextOrResource.Text -> text.text
            },
            color = Theme.colors.purple800
        )
        if (trailingIcon != null) {
            // Icon

        }
    }
}

@StaticPreview
@Composable
fun InterviewQuizScreen() {
    ScreenUI(
        TextOrResource.Resource(R.string.create_quiz_top_bar_header_text))
}

@Preview(showBackground = true)
@Composable
fun DynamicPreviewUI() {
    ScreenUI(
        TextOrResource.Resource(R.string.create_quiz_top_bar_header_text))
}

