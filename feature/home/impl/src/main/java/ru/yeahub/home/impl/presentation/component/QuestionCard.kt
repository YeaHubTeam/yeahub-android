package ru.yeahub.home.impl.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.home.impl.presentation.model.QuestionUiModel
import ru.yeahub.home.impl.presentation.screen.PreviewData
import ru.yeahub.home.impl.presentation.theme.HomeTypography

@Composable
fun QuestionCard(
    modifier: Modifier = Modifier,
    question: QuestionUiModel
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 81.dp)
            .dropShadow(
                shape = RoundedCornerShape(12.dp),
                shadow = Shadow(
                    radius = 10.dp,
                    color = Theme.colors.mainShadow,
                    offset = DpOffset(0.dp, 4.dp)
                )
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white900),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = question.question,
                style = HomeTypography.questionTitleMedium,
                color = Theme.colors.black800
            )

            Row(
                modifier = Modifier.offset(x = 1.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuestionMetric(
                    label = "Рейтинг:",
                    value = question.rating,
                    modifier = Modifier.offset(y = (-1).dp)
                )
                QuestionMetric(
                    label = "Сложность:",
                    value = question.complexity,
                    modifier = Modifier.offset(x = 1.dp, y = (-1).dp)
                )
            }
        }
    }
}

@Composable
private fun QuestionMetric(
    label: String,
    value: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Theme.colors.black10, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = HomeTypography.questionDataText,
            color = Theme.colors.black800
        )

        Box(
            modifier = Modifier
                .size(24.dp)
                .background(Theme.colors.purple700, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value.toString(),
                style = Theme.typography.body7.copy(fontSize = 12.sp, lineHeight = 16.sp),
                color = Theme.colors.white900
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 358)
@Composable
private fun QuestionCardPreview() {
    YeaHubTheme {
        QuestionCard(
            question = PreviewData.questionData,
            modifier = Modifier.padding(16.dp)
        )
    }
}
