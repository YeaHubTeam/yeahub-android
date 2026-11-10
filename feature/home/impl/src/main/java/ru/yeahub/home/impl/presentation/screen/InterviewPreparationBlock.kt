package ru.yeahub.home.impl.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.CardWithContent
import ru.yeahub.core_ui.component.LinkButton
import ru.yeahub.core_ui.component.ProgressBar
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.home.impl.R
import ru.yeahub.home.impl.domain.interview.InterviewData

/**
 * Плашка «Тренажёр собеседований» на главной: прогресс набора,
 * текущий вопрос и ссылка на прохождение.
 */
@Composable
fun InterviewPreparationBlock(
    currentProgress: Int,
    allProgress: Int,
    currentQuestion: String,
    onPassClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CardWithContent(modifier = modifier) {
        Column {
            Text(
                stringResource(R.string.home_interview_trainer_title),
                style = Theme.typography.head4,
                color = Theme.colors.black900
            )
            Spacer(Modifier.height(12.dp))
            ProgressBar(
                progress = currentProgress.toFloat() / allProgress.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(
                    R.string.home_interview_question_counter,
                    currentProgress,
                    allProgress
                ),
                style = Theme.typography.iosMed14,
                color = Theme.colors.black500
            )
            Spacer(Modifier.height(12.dp))
            Text(
                currentQuestion,
                style = Theme.typography.webMed16,
                color = Theme.colors.black900
            )
            Spacer(Modifier.height(16.dp))
            LinkButton(
                text = stringResource(R.string.home_interview_pass),
                onClick = onPassClick
            )
        }
    }
}

/** Три состояния плашки для превью: начало, середина, почти конец набора. */
class ListOfInterviewDataProvider : PreviewParameterProvider<InterviewData> {
    override val values: Sequence<InterviewData> = sequenceOf(
        InterviewData(
            currentProgress = 4,
            allProgress = 45,
            currentQuestion = "Чем val отличается от var в Kotlin?"
        ),
        InterviewData(
            currentProgress = 22,
            allProgress = 45,
            currentQuestion = "Что такое sealed class и когда его использовать?"
        ),
        InterviewData(
            currentProgress = 45,
            allProgress = 45,
            currentQuestion = "В чём разница между map и flatMap?"
        )
    )
}

@StaticPreview
@Composable
internal fun InterviewPreparationBlockPreview(
    @PreviewParameter(ListOfInterviewDataProvider::class) data: InterviewData
) {
    YeaHubTheme {
        InterviewPreparationBlock(
            currentProgress = data.currentProgress,
            allProgress = data.allProgress,
            currentQuestion = data.currentQuestion,
            onPassClick = {}
        )
    }
}
