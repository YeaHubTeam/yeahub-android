package ru.yeahub.core_ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.ui.R

private val FIGMA_LOW_PADDING = 8.dp
private val FIGMA_RADIUS = 12.dp

@Composable
fun KnownAnswerButton(
    enabled: Boolean,
    isHighlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    QuizAnswerButton(
        iconRes = R.drawable.thumbs_up_icon,
        text = TextOrResource.Resource(R.string.quiz_answer_known),
        enabled = enabled,
        isHighlighted = isHighlighted,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun UnknownAnswerButton(
    enabled: Boolean,
    isHighlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    QuizAnswerButton(
        iconRes = R.drawable.thumbs_down_icon,
        text = TextOrResource.Resource(R.string.quiz_answer_unknown),
        enabled = enabled,
        isHighlighted = isHighlighted,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
private fun QuizAnswerButton(
    iconRes: Int,
    text: TextOrResource,
    enabled: Boolean,
    isHighlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val contentColor = if (isHighlighted) {
        Theme.colors.purple700
    } else {
        Theme.colors.black700
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .width(120.dp)
            .height(48.dp),
        shape = RoundedCornerShape(FIGMA_RADIUS),
        colors = ButtonDefaults.buttonColors(
            containerColor = Theme.colors.black10,
            contentColor = contentColor,
            disabledContainerColor = Theme.colors.black10,
            disabledContentColor = contentColor
        ),
        contentPadding = PaddingValues(
            horizontal = 12.dp,
            vertical = FIGMA_LOW_PADDING
        ),
        enabled = enabled
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.padding(end = FIGMA_LOW_PADDING)
        )
        Text(
            text = when (text) {
                is TextOrResource.Text -> text.text
                is TextOrResource.Resource -> text.getString(context)
            },
            style = Theme.typography.body2
        )
    }
}

@StaticPreview
@Composable
fun KnownAnswerHighlightedButtonPreview() {
    KnownAnswerButton(
        enabled = true,
        isHighlighted = true,
        onClick = {},
        modifier = Modifier
    )
}

@StaticPreview
@Composable
fun KnownAnswerButtonPreview() {
    KnownAnswerButton(
        enabled = true,
        isHighlighted = false,
        onClick = {},
        modifier = Modifier
    )
}

@StaticPreview
@Composable
fun UnknownAnswerHighlightedButtonPreview() {
    UnknownAnswerButton(
        enabled = true,
        isHighlighted = true,
        onClick = {},
        modifier = Modifier
    )
}

@StaticPreview
@Composable
fun UnknownAnswerButtonPreview() {
    UnknownAnswerButton(
        enabled = true,
        isHighlighted = true,
        onClick = {},
        modifier = Modifier
    )
}