package ru.yeahub.core_ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.ui.R

private val contentPaddingDefault = PaddingValues(
    start = 12.dp,
    end = 12.dp,
    top = 6.dp,
    bottom = 6.dp,
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

/**
 * Базовый stateless компонент кнопки-скилла.
 *
 * Используйте специализированные обёртки вместо этого компонента напрямую:
 * [SkillButton], [SkillButtonWithBorder], [SkillButtonWithDeleteButton].
 *
 * @param onClick колбэк нажатия на кнопку.
 * @param modifier модификатор.
 * @param selected выбрана ли кнопка. Определяет цвет бордера: фиолетовый при true, серый при false.
 * @param active визуальный режим. true — цветная, false — серый фон ([CoreSkillButtonColors.inactiveContainerColor]),
 *   серый текст ([CoreSkillButtonColors.inactiveContentColor]), иконка обесцвечена.
 * @param enabled кликабельность тела кнопки. Не влияет на визуальное отображение.
 * @param showBorder отображать ли бордер. false — бордер отсутствует независимо от [selected].
 * @param text текст кнопки. Если null — текст не отображается.
 * @param leadingIconRes drawable-ресурс иконки слева. Приоритет над [leadingIconUrl].
 * @param leadingIconUrl URL иконки слева (Coil). Используется если [leadingIconRes] равен null.
 * @param trailingIcon drawable-ресурс иконки справа. Если null — иконка не отображается.
 * @param onTrailingIconClick колбэк нажатия на правую иконку.
 * @param leftIconSize размер левой иконки.
 * @param colors цвета. Используйте [CoreSkillButtonDefaults.colors] для переопределения.
 * @param shape форма кнопки. По умолчанию [CoreSkillButtonDefaults.Shape].
 * @param elevation тень кнопки.
 * @param contentPadding внутренние отступы.
 */
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
    leftIconSize: DpSize = DpSize(20.dp, 20.dp),
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
                            .height(leftIconSize.height)
                            .width(leftIconSize.width),
                        colorFilter = iconColorFilter,
                    )
                } else if (leadingIconUrl != null) {
                    AsyncImage(
                        model = leadingIconUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .height(leftIconSize.height)
                            .width(leftIconSize.width),
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

/**
 * Кнопка выбора специализации или категории.
 *
 * Поддерживает два визуальных режима, переключаемых через [active]:
 * - активный ([active] = true): цветная иконка, фиолетовый бордер при выборе, не выбранная - без бордера.
 * - неактивный ([active] = false): серый фон, обесцвеченная иконка, без бордера.
 *
 * @param text текст кнопки.
 * @param selected выбрана ли кнопка. В активном режиме определяет фиолетовый бордер.
 * @param onClick колбэк нажатия на кнопку.
 * @param modifier модификатор.
 * @param active визуальный режим. true — цветная, false — серая/обесцвеченная.
 * @param enabled кликабельность тела кнопки. Не влияет на визуальное отображение.
 * @param leadingIconRes drawable-ресурс иконки слева. Приоритет над [leadingIconUrl].
 * @param leadingIconUrl URL иконки слева (Coil). Используется если [leadingIconRes] равен null.
 * @param iconSize размер левой иконки.
 * @param contentPadding внутренние отступы.
 * @param colors цвета. Используйте [CoreSkillButtonDefaults.colors] для переопределения.
 */
@Composable
fun SkillButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    active: Boolean = true,
    enabled: Boolean = true,
    @DrawableRes leadingIconRes: Int? = null,
    leadingIconUrl: String? = null,
    iconSize: DpSize = DpSize(20.dp, 20.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    colors: CoreSkillButtonColors = CoreSkillButtonDefaults.colors(),
) {
    CoreSkillButton(
        onClick = onClick,
        modifier = modifier,
        selected = selected,
        active = active,
        enabled = enabled,
        showBorder = selected && active,
        text = text,
        leadingIconRes = leadingIconRes,
        leadingIconUrl = leadingIconUrl,
        leftIconSize = iconSize,
        colors = colors,
        contentPadding = contentPadding,
    )
}

/**
 * Кнопка выбора скилла. Бордер присутствует всегда: серый в невыбранном состоянии,
 * фиолетовый при выборе. Иконка опциональна.
 *
 * @param text текст кнопки.
 * @param selected выбрана ли кнопка. Определяет цвет бордера.
 * @param onClick колбэк нажатия на кнопку.
 * @param modifier модификатор.
 * @param enabled кликабельность тела кнопки. Не влияет на визуальное отображение.
 * @param leadingIconRes drawable-ресурс иконки слева. Приоритет над [leadingIconUrl].
 * @param leadingIconUrl URL иконки слева (Coil). Используется если [leadingIconRes] равен null.
 * @param leftIconSize размер левой иконки.
 * @param contentPadding внутренние отступы.
 * @param colors цвета. Используйте [CoreSkillButtonDefaults.colors] для переопределения.
 */
@Composable
fun SkillButtonWithBorder(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    @DrawableRes leadingIconRes: Int? = null,
    leadingIconUrl: String? = null,
    leftIconSize: DpSize = DpSize(20.dp, 20.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    colors: CoreSkillButtonColors = CoreSkillButtonDefaults.colors(),
) {
    CoreSkillButton(
        onClick = onClick,
        modifier = modifier,
        selected = selected,
        active = true,
        enabled = enabled,
        showBorder = true,
        text = text,
        leadingIconRes = leadingIconRes,
        leadingIconUrl = leadingIconUrl,
        leftIconSize = leftIconSize,
        colors = colors,
        contentPadding = contentPadding,
    )
}

/**
 * Кнопка отображения выбранного скилла с возможностью удаления.
 *
 * Всегда имеет фиолетовый бордер. Тело кнопки по умолчанию не кликабельно
 * и не реагирует на нажатия. Иконка удаления кликабельна независимо от [enabled].
 *
 * @param text текст кнопки.
 * @param onDeleteClick колбэк нажатия на иконку удаления.
 * @param modifier модификатор.
 * @param enabled кликабельность тела кнопки. Не влияет на визуальное отображение.
 *   По умолчанию false — тело некликабельно, нет ripple-эффекта.
 * @param leadingIconRes drawable-ресурс иконки слева. Приоритет над [leadingIconUrl].
 * @param leadingIconUrl URL иконки слева (Coil). Используется если [leadingIconRes] равен null.
 * @param leftIconSize размер левой иконки.
 * @param contentPadding внутренние отступы.
 * @param colors цвета. Используйте [CoreSkillButtonDefaults.colors] для переопределения.
 */
@Composable
fun SkillButtonWithDeleteButton(
    text: String,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    @DrawableRes leadingIconRes: Int? = null,
    leadingIconUrl: String? = null,
    leftIconSize: DpSize = DpSize(20.dp, 20.dp),
    @DrawableRes trailingIcon: Int = R.drawable.icon_button_close,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    colors: CoreSkillButtonColors = CoreSkillButtonDefaults.colors(),
) {
    CoreSkillButton(
        onClick = {},
        modifier = modifier,
        selected = true,
        active = true,
        enabled = enabled,
        showBorder = true,
        text = text,
        leadingIconRes = leadingIconRes,
        leadingIconUrl = leadingIconUrl,
        trailingIcon = trailingIcon,
        onTrailingIconClick = onDeleteClick,
        leftIconSize = leftIconSize,
        colors = colors,
        contentPadding = contentPadding,
    )
}

internal data class SkillButtonPreviewState(
    val name: String,
    val selected: Boolean,
    val active: Boolean,
    val hasIcon: Boolean,
)

internal class SkillButtonPreviewProvider : PreviewParameterProvider<SkillButtonPreviewState> {
    override val values = sequenceOf(
        SkillButtonPreviewState(
            "1. Не выбрана, активная",
            selected = false,
            active = true,
            hasIcon = false,
        ),
        SkillButtonPreviewState(
            "2. Выбрана, активная",
            selected = true,
            active = true,
            hasIcon = false,
        ),
        SkillButtonPreviewState(
            "3. С иконкой, не выбрана",
            selected = false,
            active = true,
            hasIcon = true,
        ),
        SkillButtonPreviewState(
            "4. С иконкой, выбрана",
            selected = true,
            active = true,
            hasIcon = true,
        ),
        SkillButtonPreviewState("5. Неактивная", selected = false, active = false, hasIcon = false),
        SkillButtonPreviewState(
            "6. Неактивная, с иконкой",
            selected = false,
            active = false,
            hasIcon = true,
        ),
    )
}

@Preview(showBackground = true)
@Composable
internal fun SkillButtonPreview(
    @PreviewParameter(SkillButtonPreviewProvider::class) state: SkillButtonPreviewState,
) {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.black25)
                .padding(16.dp),
        ) {
            SkillButton(
                text = "Figma",
                selected = state.selected,
                active = state.active,
                leadingIconRes = if (state.hasIcon) R.drawable.icon_true_button else null,
                iconSize = DpSize(20.dp, 20.dp),
                onClick = {},
            )
        }
    }
}

internal data class SkillButtonWithBorderPreviewState(
    val name: String,
    val selected: Boolean,
    val hasIcon: Boolean,
)

internal class SkillButtonWithBorderPreviewProvider :
    PreviewParameterProvider<SkillButtonWithBorderPreviewState> {
    override val values = sequenceOf(
        SkillButtonWithBorderPreviewState("1. Не выбрана", selected = false, hasIcon = false),
        SkillButtonWithBorderPreviewState("2. Выбрана", selected = true, hasIcon = false),
        SkillButtonWithBorderPreviewState(
            "3. С иконкой, не выбрана",
            selected = false,
            hasIcon = true,
        ),
        SkillButtonWithBorderPreviewState("4. С иконкой, выбрана", selected = true, hasIcon = true),
    )
}

@Preview(showBackground = true)
@Composable
internal fun SkillButtonWithBorderPreview(
    @PreviewParameter(SkillButtonWithBorderPreviewProvider::class) state: SkillButtonWithBorderPreviewState,
) {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.white900)
                .padding(16.dp),
        ) {
            SkillButtonWithBorder(
                text = "Figma",
                selected = state.selected,
                leadingIconRes = if (state.hasIcon) R.drawable.icon_true_button else null,
                leftIconSize = DpSize(20.dp, 20.dp),
                onClick = {},
            )
        }
    }
}

internal data class SkillButtonWithDeleteButtonPreviewState(
    val name: String,
    val hasIcon: Boolean,
)

internal class SkillButtonWithDeleteButtonPreviewProvider :
    PreviewParameterProvider<SkillButtonWithDeleteButtonPreviewState> {
    override val values = sequenceOf(
        SkillButtonWithDeleteButtonPreviewState("1. Без иконки", hasIcon = false),
        SkillButtonWithDeleteButtonPreviewState("2. С иконкой", hasIcon = true),
    )
}

@Preview(showBackground = true)
@Composable
internal fun SkillButtonWithDeleteButtonPreview(
    @PreviewParameter(SkillButtonWithDeleteButtonPreviewProvider::class) state: SkillButtonWithDeleteButtonPreviewState,
) {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.white900)
                .padding(16.dp),
        ) {
            SkillButtonWithDeleteButton(
                text = "Figma",
                leadingIconRes = if (state.hasIcon) R.drawable.icon_true_button else null,
                leftIconSize = DpSize(20.dp, 20.dp),
                onDeleteClick = {},
            )
        }
    }
}
