package ru.yeahub.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme

/**
 * Карточка-контейнер с мягкой тенью и внутренними отступами.
 */
@Composable
fun CardWithContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .cssShadow(
                color = Theme.colors.mainShadow,
                offsetY = 4.dp,
                blur = 10.dp,
                cornerRadius = 12.dp
            )
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.colors.white900)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
internal fun CardWithContentPreview() {
    YeaHubTheme {
        Scaffold(containerColor = Theme.colors.black25) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                CardWithContent {
                    Text(
                        text = "Контент карточки",
                        style = Theme.typography.head4,
                        color = Theme.colors.black900
                    )
                }
            }
        }
    }
}
