package ru.yeahub.home.impl.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import ru.yeahub.core_ui.component.cssShadow
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme

/**
 * Shimmer-заглушка плашки тренажёра: тот же контейнер и те же отступы/высоты,
 * что у [InterviewPreparationBlock], — серые полосы вместо контента.
 */
@Composable
fun InterviewPreparationPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .cssShadow(
                color = Theme.colors.mainShadow,
                offsetY = 4.dp,
                blur = 10.dp,
                cornerRadius = 12.dp
            )
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.colors.white900)
            .shimmer()
            .padding(16.dp)
    ) {
        Column {
            PlaceholderBar(fraction = TITLE_FRACTION, height = 24.dp)
            Spacer(Modifier.height(12.dp))
            PlaceholderBar(fraction = FULL_FRACTION, height = 8.dp)
            Spacer(Modifier.height(8.dp))
            PlaceholderBar(fraction = CAPTION_FRACTION, height = 16.dp)
            Spacer(Modifier.height(12.dp))
            PlaceholderBar(fraction = QUESTION_FRACTION, height = 20.dp)
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlaceholderBar(fraction = LINK_FRACTION, height = 20.dp)
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Theme.colors.black25)
                )
            }
        }
    }
}

@Composable
private fun PlaceholderBar(fraction: Float, height: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth(fraction)
            .height(height)
            .clip(RoundedCornerShape(4.dp))
            .background(Theme.colors.black25)
    )
}

@StaticPreview
@Composable
internal fun InterviewPreparationPlaceholderPreview() {
    YeaHubTheme {
        InterviewPreparationPlaceholder(modifier = Modifier.padding(16.dp))
    }
}

private const val FULL_FRACTION = 1f
private const val TITLE_FRACTION = 0.5f
private const val CAPTION_FRACTION = 0.4f
private const val QUESTION_FRACTION = 0.8f
private const val LINK_FRACTION = 0.35f
