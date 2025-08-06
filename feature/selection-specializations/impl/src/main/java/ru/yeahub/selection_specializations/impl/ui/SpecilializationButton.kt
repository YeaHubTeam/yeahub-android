package ru.yeahub.selection_specializations.impl.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.YeahubButtonDefaults
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.colors
import timber.log.Timber

val FIGMA_CARD_HEIGHT = 64.dp
val FIGMA_CARD_CORNER_ROUND = 8.dp
val FIGMA_CARD_ELEVATION = 4.dp //where elevation set up?

@Composable
fun SpecilializationButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    textStyle: TextStyle = LocalAppTypography.current.body3Accent,
    title: String
) {
    PrimaryButton(
        modifier = modifier
            .fillMaxWidth()
            .height(FIGMA_CARD_HEIGHT),
        onClick = onClick,
        shape = RoundedCornerShape(FIGMA_CARD_CORNER_ROUND),
        colors = YeahubButtonDefaults.primaryButtonColors(
            contentColor = colors.black900,
            containerColor = colors.white900
        ),
    ) {
        Text(
            modifier = modifier.align(Alignment.CenterVertically),
            fontSize = textStyle.fontSize,
            fontStyle = textStyle.fontStyle,
            fontWeight = textStyle.fontWeight,
            fontFamily = textStyle.fontFamily,
            textAlign = TextAlign.Center,
            text = title,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun SpecializationButtonPreview() {
    val title = "Some spec"
    SpecilializationButton(
        title = title,
        onClick = { Timber.d("Preview of Button ($title) clicked.") }
    )
}