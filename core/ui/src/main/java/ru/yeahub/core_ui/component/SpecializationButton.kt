package ru.yeahub.core_ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.colors
import ru.yeahub.core_utils.common.TextOrResource

val FIGMA_CARD_HEIGHT = 64.dp
val FIGMA_CARD_CORNER_ROUND = 8.dp
val FIGMA_CARD_ELEVATION = 4.dp

@Composable
fun SpecializationButton(
    modifier: Modifier = Modifier,
    onSpecialClick: (id: Int) -> Unit,
    textStyle: TextStyle = LocalAppTypography.current.body3Accent,
    title: TextOrResource
) {
    val context = LocalContext.current

    PrimaryButton(
        modifier = modifier
            .fillMaxWidth()
            .height(FIGMA_CARD_HEIGHT)
            .shadow(
                elevation = FIGMA_CARD_ELEVATION,
                ambientColor = colors.mainShadow
            ),
        onClick = { onSpecialClick },
        shape = RoundedCornerShape(FIGMA_CARD_CORNER_ROUND),
        colors = YeahubButtonDefaults.primaryButtonColors(
            contentColor = colors.black900,
            containerColor = colors.white900
        ),
    ) {
        val context = LocalContext.current

        Text(
            modifier = modifier.align(Alignment.CenterVertically),
            style = textStyle,
            textAlign = TextAlign.Center,
            text = title.getString(context),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@StaticPreview
@Composable
fun SpecializationButtonPreview() {
    val title = TextOrResource.Text("Some spec")

    SpecializationButton(
        title = title,
        onSpecialClick = {
            println("Example SpecilializationScreenEvent.OnSpecialClick")
        }
    )
}