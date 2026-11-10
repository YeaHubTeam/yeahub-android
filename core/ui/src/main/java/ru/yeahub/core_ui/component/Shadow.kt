package ru.yeahub.core_ui.component

import android.graphics.BlurMaskFilter
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

/**
 * Мягкая «CSS-подобная» тень (blur по маске), как в макетах Figma.
 */
fun Modifier.cssShadow(
    color: Color,
    offsetY: Dp,
    blur: Dp,
    cornerRadius: Dp
): Modifier = composed {
    val density = LocalDensity.current
    val shadowPaint = remember(color, blur, density) {
        android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
            this.color = color.toArgb()
            maskFilter = BlurMaskFilter(
                with(density) { blur.toPx() },
                BlurMaskFilter.Blur.NORMAL
            )
        }
    }
    drawBehind {
        drawIntoCanvas { canvas ->
            val radius = cornerRadius.toPx()
            val top = offsetY.toPx()
            canvas.nativeCanvas.drawRoundRect(
                0f,
                top,
                size.width,
                size.height + top,
                radius,
                radius,
                shadowPaint
            )
        }
    }
}
