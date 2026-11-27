package ru.yeahub.home.impl.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.home.impl.presentation.theme.Green900
import ru.yeahub.home.impl.presentation.theme.HomeTypography
import ru.yeahub.home.impl.presentation.theme.Yellow600
import kotlin.math.roundToInt

@Composable
fun InterviewProgressChart(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val fraction = progress.coerceIn(0f, 1f)

    Box(
        modifier = modifier.size(241.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .dropShadow(
                    shape = CircleShape,
                    shadow = Shadow(
                        radius = 10.dp,
                        color = Theme.colors.mainShadow,
                        offset = DpOffset(0.dp, 4.dp)
                    )
                )
        ) {
            val strokeWidth = 24.dp.toPx()
            val diameter = size.minDimension
            val radius = (diameter - strokeWidth) / 2
            val arcSize = Size(diameter - strokeWidth, diameter - strokeWidth)
            val arcTopLeft = Offset(center.x - radius, center.y - radius)

            drawCircle(
                color = Color(color = 0xFFFFFAEC),
                radius = diameter / 2
            )

            drawCircle(
                color = Yellow600,
                radius = radius,
                style = Stroke(width = strokeWidth)
            )

            if (fraction > 0f) {
                drawArc(
                    color = Green900,
                    startAngle = -84f,
                    sweepAngle = 360f * fraction,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round
                    )
                )
            }
        }

        Column(
            modifier = Modifier.offset(x = (-2.5).dp, y = (-1).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(fraction * 100).roundToInt()}%",
                modifier = Modifier.offset(x = (-2.5).dp),
                style = HomeTypography.progressChartText,
                color = Theme.colors.black700,
                textAlign = TextAlign.Center
            )
            Text(
                text = "пройдено",
                style = HomeTypography.progressChartText,
                color = Theme.colors.black700,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun InterviewProgressChartPreview() {
    YeaHubTheme {
        InterviewProgressChart(
            progress = 0.75f,
            modifier = Modifier.padding(16.dp)
        )
    }
}
