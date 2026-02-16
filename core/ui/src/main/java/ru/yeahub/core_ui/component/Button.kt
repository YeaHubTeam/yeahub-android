package ru.yeahub.core_ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: YeahubButtonColors = YeahubButtonDefaults.primaryButtonColors(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    DefaultButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        border = border,
        interactionSource = interactionSource,
        shape = shape,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: YeahubButtonColors = YeahubButtonDefaults.secondaryButtonColors(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    DefaultButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        border = border,
        interactionSource = interactionSource,
        shape = shape,
        contentPadding = contentPadding,
        content = content,
    )
}

@Composable
fun OutlineButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: YeahubButtonColors = YeahubButtonDefaults.outlinedButtonColors(),
    border: BorderStroke = YeahubButtonDefaults.outlineBorderDefaults(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    DefaultButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        border = border,
        interactionSource = interactionSource,
        shape = shape,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
private fun DefaultButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: YeahubButtonColors = YeahubButtonDefaults.primaryButtonColors(),
    border: BorderStroke? = null,
    shape: Shape = ButtonDefaults.shape,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val contentColor: Color by colors.contentColor(enabled)
    val containerColor: Color by colors.containerColor(enabled)

    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        border = border,
        interactionSource = interactionSource
    ) {
        CompositionLocalProvider(
            value = LocalContentColor provides contentColor
        ) {
            Row(
                modifier = Modifier
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

object YeahubButtonDefaults {
    @Composable
    fun primaryButtonColors(
        contentColor: Color = Theme.colors.white900,
        containerColor: Color = Theme.colors.purple700,
        disabledContentColor: Color = Theme.colors.white900,
        disabledContainerColor: Color = Theme.colors.black100
    ): YeahubButtonColors {
        return YeahubButtonColors(
            contentColor = contentColor,
            containerColor = containerColor,
            disabledContentColor = disabledContentColor,
            disabledContainerColor = disabledContainerColor
        )
    }

    @Composable
    fun primaryVariantButtonColors(
        contentColor: Color = Theme.colors.white900,
        containerColor: Color = Theme.colors.red600,
        disabledContentColor: Color = Theme.colors.white900,
        disabledContainerColor: Color = Theme.colors.red200
    ): YeahubButtonColors {
        return YeahubButtonColors(
            contentColor = contentColor,
            containerColor = containerColor,
            disabledContentColor = disabledContentColor,
            disabledContainerColor = disabledContainerColor
        )
    }

    @Composable
    fun secondaryButtonColors(
        contentColor: Color = Theme.colors.purple700,
        containerColor: Color = Theme.colors.purple100,
        disabledContentColor: Color = Theme.colors.black200,
        disabledContainerColor: Color = Theme.colors.black50
    ): YeahubButtonColors {
        return YeahubButtonColors(
            contentColor = contentColor,
            containerColor = containerColor,
            disabledContentColor = disabledContentColor,
            disabledContainerColor = disabledContainerColor
        )
    }

    @Composable
    fun secondaryVariantButtonColors(
        contentColor: Color = Theme.colors.white900,
        containerColor: Color = Theme.colors.red600,
        disabledContentColor: Color = Theme.colors.white900,
        disabledContainerColor: Color = Theme.colors.red200
    ): YeahubButtonColors {
        return YeahubButtonColors(
            contentColor = contentColor,
            containerColor = containerColor,
            disabledContentColor = disabledContentColor,
            disabledContainerColor = disabledContainerColor
        )
    }

    @Composable
    fun outlinedButtonColors(
        contentColor: Color = Theme.colors.red600,
        containerColor: Color = Color.Transparent,
        disabledContentColor: Color = Theme.colors.red200,
        disabledContainerColor: Color = Color.Transparent,
    ): YeahubButtonColors {
        return YeahubButtonColors(
            contentColor = contentColor,
            containerColor = containerColor,
            disabledContentColor = disabledContentColor,
            disabledContainerColor = disabledContainerColor
        )
    }

    @Composable
    fun outlineBorderDefaults(
        width: Dp = 1.dp,
        borderColor: Color = Theme.colors.red200
    ): BorderStroke {
        return BorderStroke(
            width = width,
            color = borderColor
        )
    }
}

@Immutable
data class YeahubButtonColors(
    private val contentColor: Color,
    private val containerColor: Color,
    private val disabledContentColor: Color,
    private val disabledContainerColor: Color,
) : ButtonColors {
    @Composable
    override fun containerColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) containerColor else disabledContentColor)
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) contentColor else disabledContainerColor)
    }
}

@Stable
interface ButtonColors {
    @Composable
    fun containerColor(enabled: Boolean): State<Color>

    @Composable
    fun contentColor(enabled: Boolean): State<Color>
}

@Preview(
    showBackground = true
)
@Composable
fun ButtonPreviews() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Primary Button
        Text("Primary Buttons", style = MaterialTheme.typography.titleMedium)
        PrimaryButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        ) {
            Text("Enabled Primary Button")
        }
        PrimaryButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        ) {
            Text("Disabled Primary Button")
        }

        // Secondary Button
        Text("Secondary Buttons", style = MaterialTheme.typography.titleMedium)
        SecondaryButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        ) {
            Text("Enabled Secondary Button")
        }
        SecondaryButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        ) {
            Text("Disabled Secondary Button")
        }

        // Outline Button
        Text("Outline Buttons", style = MaterialTheme.typography.titleMedium)
        OutlineButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        ) {
            Text("Enabled Outline Button")
        }
        OutlineButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        ) {
            Text("Disabled Outline Button")
        }
    }
}
