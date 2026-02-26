package ru.yeahub.core_ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
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
fun SkillButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    activeButton: Boolean = false,
    imageLeft: Int? = null,
    imageRight: Int? = null,
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
        imageLeft = imageLeft,
        imageSizeLeftHigh = imageSizeLeftHigh,
        imageSizeLeftWith = imageSizeLeftWith,
        text = text,
        fillButton = fillButton,
        buttonWithoutBackground = buttonWithoutBackground
    )
}

@Suppress("ComplexMethod")
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
            showBorder = !showBorder

            newContentColor = if (showBorder) {
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
        fillButton && enabled && activeButton -> activeBorder()
        fillButton && enabled -> null
        buttonWithoutBackground -> null
        else -> defaultsBorder()
    }
    val updatedContentColor =
        if (newContentColor == black && buttonWithoutBackground && activeButton) {
            purple
        } else if (newContentColor == black && fillButton && activeButton) {
            black
        } else if (newContentColor == Theme.colors.white900) {
            black
        } else {
            newContentColor
        }

    Surface(
        onClick = { onSurfaceClick() },
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = if (activeButton && fillButton) {
            defaultColor
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
                        val imageLeftPainter = when {
                            imageLeft == R.drawable.ellipse -> {
                                painterResource(id = R.drawable.ellipse)
                            }
                            enabled -> {
                                painterResource(id = R.drawable.icon_true_button)
                            }
                            else -> {
                                painterResource(id = R.drawable.icon_false_button)
                            }
                        }
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

data class SkillButtonParams(
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val activeButton: Boolean = false,
    val imageLeft: Int? = null,
    val imageRight: Int? = null,
    val elevation: Dp = 0.dp,
    val colors: ColorsButtonYeaHub = getButtonColors(),
    val text: String? = null,
    val buttonWithoutBackground: Boolean = false,
    val imageSizeLeftWith: Dp = 0.dp,
    val imageSizeLeftHigh: Dp = 0.dp,
    val fillButton: Boolean = false,
    val shape: Shape = RoundedCornerShape(12.dp),
    val contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
)

class SkillButtonParamsProvider : PreviewParameterProvider<SkillButtonParams> {

    override val values = sequenceOf(
        // firstButton
        SkillButtonParams(
            enabled = true,
            activeButton = false,
            contentPadding = contentPaddingDefault,
            imageLeft = R.drawable.icon_true_button,
            imageSizeLeftWith = 30.dp,
            imageSizeLeftHigh = 30.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = true,
            activeButton = true,
            contentPadding = contentPaddingDefault,
            imageLeft = R.drawable.icon_true_button,
            imageSizeLeftWith = 30.dp,
            imageSizeLeftHigh = 30.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = false,
            activeButton = false,
            contentPadding = contentPaddingDefault,
            imageLeft = R.drawable.icon_false_button,
            imageSizeLeftWith = 30.dp,
            imageSizeLeftHigh = 30.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        // withoutIconsButton
        SkillButtonParams(
            enabled = true,
            activeButton = false,
            contentPadding = contentPaddingDefault,
            imageSizeLeftWith = 30.dp,
            imageSizeLeftHigh = 30.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = true,
            activeButton = true,
            contentPadding = contentPaddingDefault,
            imageSizeLeftWith = 30.dp,
            imageSizeLeftHigh = 30.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = false,
            activeButton = false,
            contentPadding = contentPaddingDefault,
            imageSizeLeftWith = 30.dp,
            imageSizeLeftHigh = 30.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        // lendingButton
        SkillButtonParams(
            enabled = true,
            activeButton = false,
            contentPadding = contentPaddingLendingDefault,
            imageLeft = R.drawable.icon_true_button,
            fillButton = true,
            elevation = 8.dp,
            imageSizeLeftWith = 36.dp,
            imageSizeLeftHigh = 36.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = true,
            activeButton = true,
            contentPadding = contentPaddingLendingDefault,
            imageLeft = R.drawable.icon_true_button,
            fillButton = true,
            elevation = 8.dp,
            imageSizeLeftWith = 36.dp,
            imageSizeLeftHigh = 36.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = false,
            activeButton = false,
            contentPadding = contentPaddingLendingDefault,
            imageLeft = R.drawable.icon_false_button,
            fillButton = true,
            elevation = 8.dp,
            imageSizeLeftWith = 36.dp,
            imageSizeLeftHigh = 36.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        //canDelete
        SkillButtonParams(
            enabled = true,
            activeButton = false,
            contentPadding = contentPaddingDefault,
            imageLeft = R.drawable.icon_true_button,
            imageRight = R.drawable.icon_button_close,
            imageSizeLeftWith = 30.dp,
            imageSizeLeftHigh = 30.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = true,
            activeButton = true,
            contentPadding = contentPaddingDefault,
            imageLeft = R.drawable.icon_true_button,
            imageRight = R.drawable.icon_button_close,
            elevation = 8.dp,
            imageSizeLeftWith = 30.dp,
            imageSizeLeftHigh = 30.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = false,
            activeButton = false,
            contentPadding = contentPaddingDefault,
            imageLeft = R.drawable.icon_false_button,
            imageRight = R.drawable.icon_button_close,
            imageSizeLeftWith = 30.dp,
            imageSizeLeftHigh = 30.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        // buttonWithoutBackground
        SkillButtonParams(
            enabled = true,
            activeButton = false,
            contentPadding = contentPadding_Default,
            imageLeft = R.drawable.icon_true_button,
            imageSizeLeftWith = 20.dp,
            imageSizeLeftHigh = 20.dp,
            buttonWithoutBackground = true,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = true,
            activeButton = true,
            contentPadding = contentPadding_Default,
            imageLeft = R.drawable.icon_true_button,
            imageSizeLeftWith = 20.dp,
            imageSizeLeftHigh = 20.dp,
            buttonWithoutBackground = true,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = false,
            activeButton = false,
            contentPadding = contentPadding_Default,
            imageLeft = R.drawable.icon_false_button,
            imageSizeLeftWith = 20.dp,
            imageSizeLeftHigh = 20.dp,
            buttonWithoutBackground = true,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        // kompaniButton
        SkillButtonParams(
            enabled = true,
            activeButton = false,
            contentPadding = contentPaddingLendingDefault,
            imageLeft = R.drawable.ellipse,
            imageSizeLeftWith = 30.dp,
            imageSizeLeftHigh = 30.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = true,
            activeButton = true,
            contentPadding = contentPaddingLendingDefault,
            imageLeft = R.drawable.ellipse,
            imageSizeLeftWith = 30.dp,
            imageSizeLeftHigh = 30.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = false,
            activeButton = false,
            contentPadding = contentPaddingLendingDefault,
            imageLeft = R.drawable.ellipse,
            imageSizeLeftWith = 30.dp,
            imageSizeLeftHigh = 30.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        ),
        // iconsButton
        SkillButtonParams(
            enabled = true,
            activeButton = false,
            contentPadding = contentPaddingLendingDefault,
            imageLeft = R.drawable.icon_true_button,
            imageSizeLeftWith = 24.dp,
            imageSizeLeftHigh = 24.dp,
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = true,
            activeButton = true,
            contentPadding = contentPaddingLendingDefault,
            imageLeft = R.drawable.icon_true_button,
            imageSizeLeftWith = 24.dp,
            imageSizeLeftHigh = 24.dp,
            onClick = { },
            colors = getButtonColors(),
        ),
        SkillButtonParams(
            enabled = false,
            activeButton = false,
            contentPadding = contentPaddingLendingDefault,
            imageLeft = R.drawable.icon_false_button,
            imageSizeLeftWith = 24.dp,
            imageSizeLeftHigh = 24.dp,
            onClick = { },
            colors = getButtonColors(),
        )
    )
}

fun getButtonColors(): ColorsButtonYeaHub {
    return ColorsButtonYeaHub(
        contentColor = Color(color = 0xFF303030),
        containerColor = Color(color = 0xFFFFFFFF),
        disabledContentColor = Color(color = 0xFFBABABA),
        disabledContainerColor = Color(color = 0xFFFFFFFF)
    )
}

@Preview(showBackground = true)
@Composable
fun SkillButtonPreview(
    @PreviewParameter(SkillButtonParamsProvider::class) params: SkillButtonParams,
) {
    Box(modifier = Modifier.padding(10.dp)) {
        SkillButton(
            onClick = params.onClick,
            enabled = params.enabled,
            activeButton = params.activeButton,
            imageLeft = params.imageLeft,
            imageRight = params.imageRight,
            elevation = params.elevation,
            text = params.text,
            buttonWithoutBackground = params.buttonWithoutBackground,
            imageSizeLeftWith = params.imageSizeLeftWith,
            imageSizeLeftHigh = params.imageSizeLeftHigh,
            fillButton = params.fillButton,
            colors = params.colors,
            contentPadding = params.contentPadding
        )
    }
}

@Preview
@Composable
fun ExampleScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SkillButton(
            enabled = true,
            activeButton = false,
            contentPadding = contentPaddingDefault,
            imageLeft = R.drawable.icon_true_button,
            imageRight = R.drawable.icon_button_close,
            imageSizeLeftWith = 30.dp,
            imageSizeLeftHigh = 30.dp,
            text = "Figma",
            onClick = { },
            colors = getButtonColors(),
        )
    }
}
