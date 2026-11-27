package ru.yeahub.home.impl.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.home.impl.R
import ru.yeahub.home.impl.presentation.demo.HomeDemoData
import ru.yeahub.home.impl.presentation.model.InterviewHistoryUiModel
import ru.yeahub.home.impl.presentation.theme.HomeTypography

@Composable
fun InterviewHistoryCard(
    modifier: Modifier = Modifier,
    interview: InterviewHistoryUiModel
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
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
                .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = interview.interviewDate,
                style = HomeTypography.historyCardDate,
                color = Theme.colors.black500
            )

            Text(
                text = "Собеседование № ${interview.interviewNumber}",
                modifier = Modifier.offset(y = 0.5.dp),
                style = HomeTypography.historyCardTitle,
                color = Theme.colors.black900
            )

            Row(
                modifier = Modifier.offset(y = 1.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Результат:",
                    style = HomeTypography.historyResultText,
                    color = Theme.colors.black500
                )

                InterviewResult(
                    icon = R.drawable.ic_arrow_up,
                    description = "Правильные ответы",
                    count = interview.positiveCount
                )
                InterviewResult(
                    icon = R.drawable.ic_arrow_down,
                    description = "Неправильные ответы",
                    count = interview.negativeCount
                )
            }
        }
    }
}

@Composable
private fun InterviewResult(
    icon: Int,
    description: String,
    count: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = description,
            tint = Theme.colors.black500,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = count.toString(),
            style = HomeTypography.historyResultText.copy(
                fontSize = 16.sp,
                lineHeight = 20.8.sp,
                fontWeight = FontWeight.Normal
            ),
            color = Theme.colors.black700
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 358)
@Composable
private fun InterviewHistoryCardPreview() {
    YeaHubTheme {
        InterviewHistoryCard(
            interview = HomeDemoData.interviewHistory.first(),
            modifier = Modifier.padding(16.dp)
        )
    }
}
