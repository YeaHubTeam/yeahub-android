package ru.yeahub.core_ui.component

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.ButtonColorDefaults.activeBorder
import ru.yeahub.core_ui.component.ButtonColorDefaults.defaultsBorder
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
    bottom = 6.dp
)
private val contentPadding_Default = PaddingValues(
    start = 0.dp,
    end = 1.dp,
    top = 0.dp,
    bottom = 0.dp
)


@Composable
fun Test() {
    Row(modifier = Modifier.fillMaxSize()) {
        ButtonScreen()
    }
}

@Composable
fun ButtonScreen() {
    Column {
        SkillButton(
            onClick = {/* Действие при клике на button */ Log.d(
                "Main",
                "Я кликнул по большой кнопке"
            )
            },
            onRightIconClick = { /* Действие при клике на icon */ Log.d(
                "Main",
                "Я кликнул закрыть"
            )
            },
            enabled = true,
            activeButton = false,
            contentPadding = contentPaddingDefault,
            imageLeft = R.drawable.icon_true_button,
            imageSizeLeftWith = 20.dp,
            imageSizeLeftHigh = 20.dp,
            text = "Figma",
            fillButton = true
        )
    }
}

@Composable
fun SkillButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    activeButton: Boolean = false,
    imageLeft: Int? = null, // Иконка слева
    imageRight: Int? = null, // Иконка справа
    elevation: Dp = 0.dp,
    text: String? = null,
    buttonWithoutBackground: Boolean = false,
    imageSizeLeftWith: Dp = 0.dp,
    imageSizeLeftHigh: Dp = 0.dp,
    fillButton: Boolean = false,
    colors: ColorsButtonYeaHub = ButtonColorDefaults.firstButtonColors(),
    shape: Shape = RoundedCornerShape(12.dp),
    onRightIconClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) {
    val image = when {
        imageLeft != null && enabled && imageLeft == R.drawable.icon_true_button -> R.drawable.icon_true_button
        imageLeft != null && !enabled && imageLeft == R.drawable.icon_false_button -> R.drawable.icon_false_button
        imageLeft != null && imageLeft == R.drawable.ellipse -> R.drawable.ellipse
        else -> null
    }
    DefaultButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        activeButton = activeButton,
        colors = colors,
        elevation = elevation,
        shape = shape,
        contentPadding = contentPadding,
        onRightIconClick = onRightIconClick,
        imageRight = imageRight,
        imageLeft = image,
        imageSizeLeftHigh = imageSizeLeftHigh,
        imageSizeLeftWith = imageSizeLeftWith,
        text = text,
        fillButton = fillButton,
        buttonWithoutBackground = buttonWithoutBackground
    )
}

@Composable
fun DefaultButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    elevation: Dp = 0.dp,
    fillButton: Boolean = false,
    imageSizeLeftWith: Dp = 30.dp,
    imageSizeLeftHigh: Dp = 30.dp,
    activeButton: Boolean = false,
    colors: ColorsButtonYeaHub = ButtonColorDefaults.firstButtonColors(),
    buttonWithoutBackground: Boolean = false,
    shape: Shape = ButtonDefaults.shape,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    text: String? = null,
    imageLeft: Int? = null,
    imageRight: Int? = null,
    onRightIconClick: (() -> Unit)? = null,
) {
    val defaultColor: Color = Theme.colors.white900
    val purple = Theme.colors.purple700
    val black = Theme.colors.black800

    var showBorder by remember { mutableStateOf(activeButton) }
    val interactionSource = remember { MutableInteractionSource() }

    val containerColor by colors.containerColor(enabled)
    val contentColor by colors.contentColor(enabled)

    var newContainerColor by remember { mutableStateOf(containerColor) }
    var newContentColor by remember { mutableStateOf(contentColor) }

    val onSurfaceClick: () -> Unit = {
        if (fillButton) {
            newContainerColor = if (newContainerColor == defaultColor) {
                purple
            } else {
                defaultColor
            }
            newContentColor = if (newContentColor == black) {
                defaultColor
            } else {
                black
            }
        }
        if (enabled) {
            showBorder = !showBorder
        }
        if (buttonWithoutBackground) {
            newContentColor = if (newContentColor == black) {
                purple
            } else {
                black
            }
        }
        onClick()
    }
    val border = when {
        showBorder && !fillButton && !buttonWithoutBackground -> activeBorder()
        fillButton && enabled -> null
        buttonWithoutBackground -> null
        else -> defaultsBorder()
    }
    val updatedContentColor =
        if (newContentColor == black && buttonWithoutBackground && activeButton) {
            purple
        } else if (newContentColor == black && fillButton&& activeButton) {
            defaultColor
        } else {
            newContentColor
        }

    Surface(
        onClick = { onSurfaceClick() },
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = if (activeButton && fillButton) {
            purple
        } else {
            if (buttonWithoutBackground) Color.Transparent else newContainerColor
        },
        contentColor = updatedContentColor,
        border = border,
        interactionSource = interactionSource,
        shadowElevation = elevation
    ) {
        CompositionLocalProvider(LocalContentColor provides updatedContentColor) {
            Row(
                modifier = Modifier.padding(contentPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                content = {
                    imageLeft?.let {
                        val imageLeftPainter: Painter = painterResource(id = imageLeft)
                        Image(
                            modifier = Modifier
                                .height(imageSizeLeftHigh)
                                .width(imageSizeLeftWith),
                            painter = imageLeftPainter,
                            contentDescription = "Left Icon",
                        )
                    }
                    text?.let {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = text,
                            style = Theme.typography.body3Accent,
                        )
                    }
                    imageRight?.let {
                        Spacer(modifier = Modifier.width(8.dp))
                        val imageRPainter: Painter = painterResource(id = imageRight)
                        Image(
                            modifier = Modifier
                                .height(20.dp)
                                .width(20.dp)
                                .clickable {
                                    onRightIconClick?.invoke()
                                },
                            painter = imageRPainter,
                            contentDescription = "Close",
                        )
                    }
                }
            )
        }
    }
}

object ButtonColorDefaults {
    @Composable
    fun firstButtonColors(
        contentColor: Color = Theme.colors.black800,
        containerColor: Color = Theme.colors.white900,
        disabledContentColor: Color = Theme.colors.black200,
        disabledContainerColor: Color = Theme.colors.white900,
    ): ColorsButtonYeaHub {
        return ColorsButtonYeaHub(
            contentColor = contentColor,
            containerColor = containerColor,
            disabledContentColor = disabledContentColor,
            disabledContainerColor = disabledContainerColor
        )
    }

    @Composable
    fun defaultsBorder(
        width: Dp = 1.dp,
        borderColor: Color = Theme.colors.black200,
    ): BorderStroke {
        return BorderStroke(
            width = width,
            color = borderColor
        )
    }

    @Composable
    fun activeBorder(
        width: Dp = 1.dp,
        borderColor: Color = Theme.colors.purple700,
    ): BorderStroke {
        return BorderStroke(
            width = width,
            color = borderColor
        )
    }
}

// Класс для управления цветами кнопок
@Immutable
data class ColorsButtonYeaHub(
    private val contentColor: Color,
    private val containerColor: Color,
    private val disabledContentColor: Color,
    private val disabledContainerColor: Color,
) : ButtonColors1 {

    @Composable
    override fun containerColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) containerColor else disabledContainerColor)
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) contentColor else disabledContentColor)
    }
}


@Stable
interface ButtonColors1 {
    @Composable
    fun containerColor(enabled: Boolean): State<Color>

    @Composable
    fun contentColor(enabled: Boolean): State<Color>
}