package ru.yeahub.core_ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.ui.R

private val contentPaddingDefault = PaddingValues(
    start = 12.dp,
    end = 12.dp,
    top = 6.dp,
    bottom = 6.dp
)
private val contentPaddingLendingDefault = PaddingValues(
    start = 6.dp,
    end = 6.dp,
    top = 6.dp,
    bottom = 6.dp,
)
private val contentPadding_Default = PaddingValues(
    start = 0.dp,
    end = 1.dp,
    top = 0.dp,
    bottom = 0.dp,
)

@Immutable
data class CoreSkillButtonColors(
    val activeContainerColor: Color,
    val inactiveContainerColor: Color,
    val activeContentColor: Color,
    val inactiveContentColor: Color,
    val selectedBorderColor: Color,
    val unselectedBorderColor: Color,
)

object CoreSkillButtonDefaults {
    val Shape: Shape = RoundedCornerShape(12.dp)

    @Composable
    fun colors(
        activeContainerColor: Color = Theme.colors.white900,
        inactiveContainerColor: Color = Theme.colors.black50,
        activeContentColor: Color = Theme.colors.black800,
        inactiveContentColor: Color = Theme.colors.black600,
        selectedBorderColor: Color = Theme.colors.purple700,
        unselectedBorderColor: Color = Theme.colors.black200,
    ): CoreSkillButtonColors = CoreSkillButtonColors(
        activeContainerColor = activeContainerColor,
        inactiveContainerColor = inactiveContainerColor,
        activeContentColor = activeContentColor,
        inactiveContentColor = inactiveContentColor,
        selectedBorderColor = selectedBorderColor,
        unselectedBorderColor = unselectedBorderColor,
    )
}

@Composable
fun CoreSkillButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    active: Boolean = true,
    enabled: Boolean = true,
    showBorder: Boolean = true,
    text: String? = null,
    @DrawableRes leadingIconRes: Int? = null,
    leadingIconUrl: String? = null,
    @DrawableRes trailingIcon: Int? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    iconSize: DpSize = DpSize(30.dp, 30.dp),
    colors: CoreSkillButtonColors = CoreSkillButtonDefaults.colors(),
    shape: Shape = CoreSkillButtonDefaults.Shape,
    elevation: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
) {
    val border = when {
        !showBorder -> null
        selected -> BorderStroke(1.dp, colors.selectedBorderColor)
        else -> BorderStroke(1.dp, colors.unselectedBorderColor)
    }

    val containerColor = if (active) colors.activeContainerColor else colors.inactiveContainerColor
    val contentColor = if (active) colors.activeContentColor else colors.inactiveContentColor

    val iconColorFilter = if (!active) {
        ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
    } else {
        null
    }

    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        border = border,
        shadowElevation = elevation,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Row(
                modifier = Modifier.padding(contentPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                if (leadingIconRes != null) {
                    Image(
                        painter = painterResource(id = leadingIconRes),
                        contentDescription = null,
                        modifier = Modifier
                            .height(iconSize.height)
                            .width(iconSize.width),
                        colorFilter = iconColorFilter,
                    )
                } else if (leadingIconUrl != null) {
                    AsyncImage(
                        model = leadingIconUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .height(iconSize.height)
                            .width(iconSize.width),
                        colorFilter = iconColorFilter,
                    )
                }

                text?.let {
                    if (leadingIconRes != null || leadingIconUrl != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        text = it,
                        style = Theme.typography.body3Accent,
                    )
                }

                trailingIcon?.let {
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                            .clickable { onTrailingIconClick?.invoke() },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SkillButtonActivePreview() {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CoreSkillButton(
            text = "Figma",
            selected = true,
            leadingIconRes = R.drawable.icon_true_button,
            iconSize = DpSize(20.dp, 20.dp),
            onClick = {},
        )
        CoreSkillButton(
            text = "Figma",
            selected = true,
            leadingIconRes = R.drawable.icon_true_button,
            trailingIcon = R.drawable.icon_button_close,
            iconSize = DpSize(20.dp, 20.dp),
            onClick = {},
        )
        CoreSkillButton(
            text = "Figma",
            selected = true,
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SkillButtonDefaultPreview() {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CoreSkillButton(
            text = "Figma",
            leadingIconRes = R.drawable.icon_true_button,
            iconSize = DpSize(20.dp, 20.dp),
            onClick = {},
        )
        CoreSkillButton(
            text = "Figma",
            onClick = {},
        )
        CoreSkillButton(
            text = "Figma",
            showBorder = false,
            elevation = 8.dp,
            leadingIconRes = R.drawable.icon_true_button,
            iconSize = DpSize(20.dp, 20.dp),
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SkillButtonInactivePreview() {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CoreSkillButton(
            text = "Figma",
            active = false,
            leadingIconRes = R.drawable.icon_true_button,
            iconSize = DpSize(20.dp, 20.dp),
            onClick = {},
        )
        CoreSkillButton(
            text = "Figma",
            active = false,
            onClick = {},
        )
        CoreSkillButton(
            text = "Figma",
            active = false,
            leadingIconRes = R.drawable.icon_true_button,
            trailingIcon = R.drawable.icon_button_close,
            iconSize = DpSize(20.dp, 20.dp),
            onClick = {},
        )
    }
}
