package ru.yeahub.core_ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.ui.R


@Composable
fun UploadPhotoButton(
    modifier: Modifier = Modifier,
    title: String = "Кликни для изменения",
    helper: String = "JPG, PNG, JPEG (не более 5мб)",
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(12.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(164.dp)
            .dashedRoundedBorder(
                color = Theme.colors.purple400,
                strokeWidth = 2.dp,
                cornerRadius = 12.dp,
                dashLength = 8.dp,
                gapLength = 8.dp,
            )
            .clickable(onClick = onClick),
        shape = shape,
        color = Color.Transparent,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.image),
                contentDescription = null,
                tint = Theme.colors.purple700,
                modifier = Modifier.size(32.dp),
            )

            Spacer(Modifier.height(12.dp))

            Text(text = title, style = Theme.typography.body3, color = Theme.colors.purple700)

            Spacer(Modifier.height(12.dp))

            Text(text = helper, style = Theme.typography.body7, color = Theme.colors.black200)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UUploadPhotoButtonPreview() {
    Column(Modifier.padding(16.dp)) {
        UploadPhotoButton(onClick = {})
    }
}

fun Modifier.dashedRoundedBorder(
    color: Color,
    strokeWidth: Dp = 2.dp,
    cornerRadius: Dp = 12.dp,
    dashLength: Dp = 12.dp,
    gapLength: Dp = 12.dp,
) = drawWithCache {
    val strokePx = strokeWidth.toPx()
    val radiusPx = cornerRadius.toPx()
    val dashPx = dashLength.toPx()
    val gapPx = gapLength.toPx()

    val stroke = Stroke(
        width = strokePx,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashPx, gapPx), 0f),
    )

    onDrawBehind {
        drawRoundRect(
            color = color,
            style = stroke,
            cornerRadius = CornerRadius(radiusPx, radiusPx),
        )
    }
}