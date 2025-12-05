package ru.yeahub.core_ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.ui.R

private object PopoverQuestionControlConstants {
    val MAX_WIDTH = 600.dp
    val LARGE_SCREEN_THRESHOLD = 440.dp
    val LARGE_ROW_SPACING = 32.dp
    val SMALL_ROW_SPACING = 10.dp
    val CORNER_RADIUS = 16.dp
    val SHADOW_ELEVATION = 10.dp
    val CONTENT_PADDING_VERTICAL = 16.dp
    val CONTENT_PADDING_HORIZONTAL = 32.dp
    val ICON_TEXT_SPACING = 2.dp
}

enum class FavoriteState {
    DISABLED,     // Серый - нельзя добавить
    AVAILABLE,    // Черный - можно добавить
    FAVORITED;    // Красная иконка + черный текст - уже добавлено

    val isClickable: Boolean
        get() = this != DISABLED
}

/**
 * @param onLearnClick Callback для кнопки "Изучить". Если null, кнопка будет неактивна (серая)
 * @param onRepeatClick Callback для кнопки "Повторить". Если null, кнопка будет неактивна (серая)
 * @param onFavoriteClick Callback для кнопки "Избранное". Если null, кнопка будет неактивна (серая)
 * @param onPreviousClick Callback для кнопки "Назад". Если null, кнопка будет неактивна (серая)
 * @param onNextClick Callback для кнопки "Вперед". Если null, кнопка будет неактивна (серая)
 * @param favoriteState Состояние кнопки избранного (недоступно/доступно/добавлено)
 */

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PopoverQuestion(
    modifier: Modifier = Modifier,
    favoriteState: FavoriteState,
    onLearnClick: (() -> Unit)? = null,
    onRepeatClick: (() -> Unit)? = null,
    onFavoriteClick: (() -> Unit)? = null,
    onPreviousClick: (() -> Unit)? = null,
    onNextClick: (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier
            .wrapContentWidth()
            .widthIn(max = PopoverQuestionControlConstants.MAX_WIDTH),
        shape = RoundedCornerShape(PopoverQuestionControlConstants.CORNER_RADIUS),
        shadowElevation = PopoverQuestionControlConstants.SHADOW_ELEVATION,
        color = Theme.colors.white900,
    ) {
        BoxWithConstraints {
            val screenWidth = maxWidth
            val rowSpacing = remember(screenWidth) {
                if (screenWidth > PopoverQuestionControlConstants.LARGE_SCREEN_THRESHOLD) {
                    PopoverQuestionControlConstants.LARGE_ROW_SPACING
                } else {
                    PopoverQuestionControlConstants.SMALL_ROW_SPACING
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        top = PopoverQuestionControlConstants.CONTENT_PADDING_VERTICAL,
                        start = PopoverQuestionControlConstants.CONTENT_PADDING_HORIZONTAL,
                        bottom = PopoverQuestionControlConstants.CONTENT_PADDING_VERTICAL,
                        end = PopoverQuestionControlConstants.CONTENT_PADDING_HORIZONTAL
                    ),
                verticalArrangement = Arrangement.spacedBy(rowSpacing),
            ) {
                // Первый ряд: Изучить, Повторить, Избранное
                if (false) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        IconWithText(
                            iconResId = R.drawable.student,
                            text = stringResource(R.string.learn),
                            contentDescription = stringResource(R.string.learn),
                            isClickable = onLearnClick != null,
                            onItemClickListener = { onLearnClick?.invoke() },
                            screenWidth = screenWidth
                        )
                        IconWithText(
                            iconResId = R.drawable.clockcounterclockwise,
                            text = stringResource(R.string.repeat),
                            contentDescription = stringResource(R.string.repeat),
                            isClickable = onRepeatClick != null,
                            onItemClickListener = { onRepeatClick?.invoke() },
                            screenWidth = screenWidth
                        )
                        IconWithText(
                            iconResId = getFavoriteIconRes(favoriteState),
                            text = stringResource(R.string.favorite),
                            contentDescription = stringResource(R.string.favorite),
                            isClickable = favoriteState.isClickable,
                            iconColor = getFavoriteIconColor(favoriteState),
                            textColor = getFavoriteTextColor(favoriteState),
                            onItemClickListener = { onFavoriteClick?.invoke() },
                            screenWidth = screenWidth
                        )
                    }
                }
                // Второй ряд: Назад, Вперед
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconWithText(
                        iconResId = R.drawable.alt_arrow_left,
                        text = stringResource(R.string.previous),
                        contentDescription = stringResource(R.string.previous),
                        isClickable = onPreviousClick != null,
                        onItemClickListener = { onPreviousClick?.invoke() },
                        screenWidth = screenWidth
                    )
                    IconWithText(
                        iconResId = R.drawable.alt_arrow_right,
                        text = stringResource(R.string.next),
                        contentDescription = stringResource(R.string.next),
                        iconFirst = false,
                        isClickable = onNextClick != null,
                        onItemClickListener = { onNextClick?.invoke() },
                        screenWidth = screenWidth
                    )
                }
            }
        }
    }
}

@Composable
fun IconWithText(
    iconResId: Int,
    text: String,
    contentDescription: String,
    isClickable: Boolean,
    onItemClickListener: () -> Unit,
    screenWidth: Dp,
    iconFirst: Boolean = true,
    iconColor: Color? = null,
    textColor: Color? = null,
) {
    val actualIconColor = iconColor ?: getColorForIcon(isClickable)
    val actualTextColor = textColor ?: getColorForText(isClickable)
    val textStyle = getTextStyleForIconAndText(screenWidth)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(PopoverQuestionControlConstants.ICON_TEXT_SPACING),
        modifier = Modifier
            .clickable(enabled = isClickable) { onItemClickListener() }
            .semantics { role = Role.Button }
    ) {
        if (iconFirst) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = contentDescription,
                tint = actualIconColor
            )
            Text(
                text = text,
                color = actualTextColor,
                style = textStyle
            )
        } else {
            Text(
                text = text,
                color = actualTextColor,
                style = textStyle
            )
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = contentDescription,
                tint = actualIconColor
            )
        }
    }
}

@Composable
private fun getColorForIcon(isClickable: Boolean): Color {
    return if (isClickable) {
        Theme.colors.black600
    } else {
        Theme.colors.black200
    }
}

@Composable
private fun getColorForText(isClickable: Boolean): Color {
    return if (isClickable) {
        Theme.colors.black800
    } else {
        Theme.colors.black200
    }
}

@Composable
private fun getTextStyleForIconAndText(screenWidth: Dp): TextStyle {
    return if (screenWidth > PopoverQuestionControlConstants.LARGE_SCREEN_THRESHOLD) {
        Theme.typography.body3Strong
    } else {
        Theme.typography.body2
    }
}

@Composable
private fun getFavoriteIconColor(favoriteState: FavoriteState): Color {
    return when (favoriteState) {
        FavoriteState.DISABLED -> Theme.colors.black200
        FavoriteState.AVAILABLE -> Theme.colors.black600
        FavoriteState.FAVORITED -> Theme.colors.red700
    }
}

private fun getFavoriteIconRes(favoriteState: FavoriteState): Int {
    return when (favoriteState) {
        FavoriteState.DISABLED -> R.drawable.fav_disabled
        FavoriteState.AVAILABLE -> R.drawable.icon
        FavoriteState.FAVORITED -> R.drawable.vector__stroke_
    }
}

@Composable
private fun getFavoriteTextColor(favoriteState: FavoriteState): Color {
    return when (favoriteState) {
        FavoriteState.DISABLED -> Theme.colors.black200
        FavoriteState.AVAILABLE -> Theme.colors.black800
        FavoriteState.FAVORITED -> Theme.colors.black800
    }
}

@StaticPreview
@Composable
fun PopoverQuestionPreviewFavoriteStateDISABLED() {
    PopoverQuestion(
        onLearnClick = null,
        onRepeatClick = { },
        onFavoriteClick = { },
        onPreviousClick = { },
        onNextClick = { },
        favoriteState = FavoriteState.DISABLED,
    )
}

@StaticPreview
@Composable
fun PopoverQuestionPreviewFavoriteStateFAVORITED() {
    PopoverQuestion(
        onLearnClick = null,
        onRepeatClick = { },
        onFavoriteClick = { },
        onPreviousClick = { },
        onNextClick = { },
        favoriteState = FavoriteState.FAVORITED,
    )
}

@StaticPreview
@Composable
fun PopoverQuestionPreviewFavoriteStateAVAILABLE() {
    PopoverQuestion(
        onLearnClick = null,
        onRepeatClick = { },
        onFavoriteClick = { },
        onPreviousClick = { },
        onNextClick = { },
        favoriteState = FavoriteState.AVAILABLE,
    )
}