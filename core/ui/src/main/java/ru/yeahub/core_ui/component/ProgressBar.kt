package ru.yeahub.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme

/**
 * Горизонтальный прогресс-бар в виде скруглённой полосы.
 *
 * Ширину полосы компонент сам не задаёт — передайте её снаружи модификатором,
 * например `Modifier.fillMaxWidth()`, иначе трек схлопнется по ширине.
 *
 * @param progress доля заполнения, 0f..1f
 */
@Composable
fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = Theme.colors.purple300,
    progressColor: Color = Theme.colors.purple700,
    barHeight: Dp = 8.dp
) {
    Box(
        modifier = modifier
            .height(barHeight)
            .clip(RoundedCornerShape(percent = 50))
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .clip(RoundedCornerShape(percent = 50))
                .background(progressColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun ProgressBarPreview() {
    YeaHubTheme {
        Scaffold(containerColor = Theme.colors.black25) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                CardWithContent {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ProgressBar(
                            progress = 0.6f,
                            modifier = Modifier.fillMaxWidth()
                        )
                        ProgressBar(
                            progress = 0.15f,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
