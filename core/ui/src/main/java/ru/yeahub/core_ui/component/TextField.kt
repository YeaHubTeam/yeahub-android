package ru.yeahub.core_ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.core_utils.common.TextOrResource

/**
 * Набор цветов для текстовых полей YeaHub.
 *
 * Используйте [YeahubTextFieldDefaults.colors] для создания экземпляра
 * с дефолтными значениями из темы, переопределяя только нужные цвета.
 */
@Immutable
data class YeahubTextFieldColors(
    val focusedTextColor: Color,
    val unfocusedTextColor: Color,
    val disabledTextColor: Color,
    val focusedBorderColor: Color,
    val unfocusedBorderColor: Color,
    val disabledBorderColor: Color,
    val errorBorderColor: Color,
    val cursorColor: Color,
    val focusedLeadingIconColor: Color,
    val unfocusedLeadingIconColor: Color,
    val focusedTrailingIconColor: Color,
    val unfocusedTrailingIconColor: Color,
    val titleColor: Color,
    val placeholderColor: Color,
    val errorColor: Color,
)

object YeahubTextFieldDefaults {
    val Shape: Shape = RoundedCornerShape(12.dp)

    @Composable
    fun colors(
        focusedTextColor: Color = Theme.colors.black900,
        unfocusedTextColor: Color = Theme.colors.black900,
        disabledTextColor: Color = Theme.colors.black500,
        focusedBorderColor: Color = Theme.colors.purple700,
        unfocusedBorderColor: Color = Theme.colors.black100,
        disabledBorderColor: Color = Theme.colors.black100,
        errorBorderColor: Color = Theme.colors.red700,
        cursorColor: Color = Theme.colors.purple700,
        focusedLeadingIconColor: Color = Theme.colors.black900,
        unfocusedLeadingIconColor: Color = Theme.colors.black500,
        focusedTrailingIconColor: Color = Theme.colors.black900,
        unfocusedTrailingIconColor: Color = Theme.colors.black500,
        titleColor: Color = Theme.colors.black900,
        placeholderColor: Color = Theme.colors.black500,
        errorColor: Color = Theme.colors.red700,
    ): YeahubTextFieldColors = YeahubTextFieldColors(
        focusedTextColor = focusedTextColor,
        unfocusedTextColor = unfocusedTextColor,
        disabledTextColor = disabledTextColor,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        disabledBorderColor = disabledBorderColor,
        errorBorderColor = errorBorderColor,
        cursorColor = cursorColor,
        focusedLeadingIconColor = focusedLeadingIconColor,
        unfocusedLeadingIconColor = unfocusedLeadingIconColor,
        focusedTrailingIconColor = focusedTrailingIconColor,
        unfocusedTrailingIconColor = unfocusedTrailingIconColor,
        titleColor = titleColor,
        placeholderColor = placeholderColor,
        errorColor = errorColor,
    )
}

// 1. БАЗА (дизайн YeaHub)

@Composable
internal fun CoreTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String? = null,
    error: TextOrResource? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    colors: YeahubTextFieldColors = YeahubTextFieldDefaults.colors(),
    shape: Shape = YeahubTextFieldDefaults.Shape,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    val errorText = error?.let { error ->
        when (error) {
            is TextOrResource.Resource -> stringResource(error.resource)
            is TextOrResource.Text -> error.text
        }
    }

    val textFieldValueState = remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = if (textFieldValueState.value.text == value) {
        textFieldValueState.value
    } else {
        TextFieldValue(text = value, selection = TextRange(value.length))
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (title != null) {
            Text(
                text = title,
                style = Theme.typography.body2Accent,
                color = colors.titleColor
            )
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { onFocusChanged(it.isFocused) }
                .then(
                    if (errorText != null) {
                        Modifier.semantics { error(errorText) }
                    } else {
                        Modifier
                    }
                ),
            value = textFieldValue,
            onValueChange = { newTextFieldValue ->
                textFieldValueState.value = newTextFieldValue
                if (newTextFieldValue.text != value) {
                    onValueChange(newTextFieldValue.text)
                }
            },
            enabled = enabled,
            isError = error != null,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            placeholder = placeholder?.let {
                { Text(text = it, style = Theme.typography.body2, color = colors.placeholderColor) }
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = singleLine,
            shape = shape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colors.focusedTextColor,
                unfocusedTextColor = colors.unfocusedTextColor,
                disabledTextColor = colors.disabledTextColor,
                focusedBorderColor = colors.focusedBorderColor,
                unfocusedBorderColor = colors.unfocusedBorderColor,
                disabledBorderColor = colors.disabledBorderColor,
                errorBorderColor = colors.errorBorderColor,
                cursorColor = colors.cursorColor,
                focusedLeadingIconColor = colors.focusedLeadingIconColor,
                unfocusedLeadingIconColor = colors.unfocusedLeadingIconColor,
                focusedTrailingIconColor = colors.focusedTrailingIconColor,
                unfocusedTrailingIconColor = colors.unfocusedTrailingIconColor,
            )
        )

        AnimatedVisibility(
            visible = errorText != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            if (errorText != null) {
                Text(
                    text = errorText,
                    color = colors.errorColor,
                    style = Theme.typography.body1
                )
            }
        }
    }
}

// 2. Основное текстовое поле дизайн-системы YeaHub

/**
 * @param value текущее значение поля.
 * @param onValueChange колбэк при изменении текста.
 * @param modifier модификатор для корневого контейнера.
 * @param title заголовок над полем (опционально).
 * @param placeholder текст-подсказка (опционально).
 * @param error текст ошибки; если не null — поле подсвечивается красным
 *   и текст ошибки отображается под полем с анимацией.
 * @param leadingIcon иконка слева (опционально).
 * @param leadingIconContentDescription описание leading-иконки для Accessibility / TalkBack.
 * @param trailingIcon иконка справа (опционально).
 * @param trailingIconContentDescription описание trailing-иконки для Accessibility / TalkBack.
 * @param onTrailingIconClick колбэк нажатия на trailing-иконку.
 *   Если null — иконка отображается без кликабельности.
 * @param visualTransformation трансформация отображения (например, [PasswordVisualTransformation]).
 * @param keyboardOptions опции клавиатуры.
 * @param keyboardActions действия клавиатуры.
 * @param singleLine однострочное поле.
 * @param enabled доступность для ввода.
 * @param colors набор цветов. Используйте [YeahubTextFieldDefaults.colors]
 *   для точечного переопределения.
 * @param onFocusChanged колбэк при изменении фокуса.
 */
@Composable
fun PrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String? = null,
    error: TextOrResource? = null,
    leadingIcon: Painter? = null,
    leadingIconContentDescription: String? = null,
    trailingIcon: Painter? = null,
    trailingIconContentDescription: String? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    colors: YeahubTextFieldColors = YeahubTextFieldDefaults.colors(),
    onFocusChanged: (Boolean) -> Unit = {},
) {
    CoreTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        title = title,
        placeholder = placeholder,
        error = error,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        enabled = enabled,
        colors = colors,
        onFocusChanged = onFocusChanged,
        leadingIcon = leadingIcon?.let { icon ->
            {
                Icon(
                    painter = icon,
                    contentDescription = leadingIconContentDescription,
                    tint = colors.unfocusedLeadingIconColor
                )
            }
        },
        trailingIcon = trailingIcon?.let { icon ->
            {
                if (onTrailingIconClick != null) {
                    IconButton(onClick = onTrailingIconClick, enabled = enabled) {
                        Icon(
                            painter = icon,
                            contentDescription = trailingIconContentDescription,
                            tint = colors.unfocusedTrailingIconColor
                        )
                    }
                } else {
                    Icon(
                        painter = icon,
                        contentDescription = trailingIconContentDescription,
                        tint = colors.unfocusedTrailingIconColor
                    )
                }
            }
        }
    )
}

// 3. ОБЕРТКА ДЛЯ ПОИСКА С САДЖЕСТАМИ

/**
 * @param value текущее значение поля.
 * @param onValueChange колбэк при изменении текста.
 * @param modifier модификатор для корневого контейнера.
 * @param title заголовок над полем (опционально).
 * @param placeholder текст-подсказка.
 * @param suggestions список предложений для отображения.
 * @param onSuggestionClick колбэк при выборе предложения.
 * @param isExpanded управление видимостью списка. По умолчанию — виден, если [suggestions] не пуст.
 * @param enabled доступность для ввода.
 * @param colors набор цветов. Используйте [YeahubTextFieldDefaults.colors]
 *   для точечного переопределения.
 * @param onSearch колбэк при нажатии кнопки "Поиск" на клавиатуре.
 */
@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String,
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit,
    isExpanded: Boolean = suggestions.isNotEmpty(),
    enabled: Boolean = true,
    colors: YeahubTextFieldColors = YeahubTextFieldDefaults.colors(),
    onSearch: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val filteredSuggestions = suggestions.filter { !it.equals(value, ignoreCase = true) }
    val showSuggestions = isExpanded && filteredSuggestions.isNotEmpty()

    Column(modifier = modifier.fillMaxWidth()) {
        CoreTextField(
            value = value,
            title = title,
            onValueChange = onValueChange,
            placeholder = placeholder,
            enabled = enabled,
            colors = colors,
            leadingIcon = {
                Icon(
                    painter = rememberVectorPainter(Icons.Default.Search),
                    contentDescription = null,
                    tint = colors.unfocusedLeadingIconColor
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                    keyboardController?.hide()
                    focusManager.clearFocus(force = true)
                }
            )
        )

        AnimatedVisibility(visible = showSuggestions) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = MaterialTheme.shapes.medium,
                color = Theme.colors.white900,
                border = BorderStroke(1.dp, Theme.colors.black50),
                shadowElevation = 4.dp
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                ) {
                    items(filteredSuggestions) { suggestion ->
                        Text(
                            text = suggestion,
                            style = Theme.typography.body3,
                            color = Theme.colors.black900,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSuggestionClick(suggestion)
                                    keyboardController?.hide()
                                    focusManager.clearFocus(force = true)
                                }
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// Previews

internal data class TextFieldState(
    val name: String,
    val value: String,
    val title: String? = null,
    val placeholder: String? = null,
    val error: TextOrResource? = null,
    val isPassword: Boolean = false,
    val hasAction: Boolean = false,
    val enabled: Boolean = true
)

internal class TextFieldProvider : PreviewParameterProvider<TextFieldState> {
    override val values = sequenceOf(
        TextFieldState("1. Default", "", "Email", "example@mail.com"),
        TextFieldState("2. Filled + Action", "Текст", "Поиск", hasAction = true),
        TextFieldState("3. Password", "password123", "Пароль", isPassword = true),
        TextFieldState("4. Error", "123", "Логин", error = TextOrResource.Text("Ошибка валидации")),
        TextFieldState("5. Disabled", "Заблокировано", enabled = false),
        TextFieldState("6. No Title", "Просто поле")
    )
}

// Статичные превью для всех вариантов дизайна
@Preview(showBackground = true)
@Composable
internal fun PrimaryTextFieldStaticPreview(
    @PreviewParameter(TextFieldProvider::class) state: TextFieldState
) {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.white900)
                .padding(16.dp)
        ) {
            val visualTransformation =
                if (state.isPassword) PasswordVisualTransformation() else VisualTransformation.None
            val actionIcon = when {
                state.isPassword -> rememberVectorPainter(Icons.Filled.Visibility)
                state.hasAction -> rememberVectorPainter(Icons.Filled.Clear)
                else -> null
            }
            PrimaryTextField(
                title = state.title,
                placeholder = state.placeholder,
                value = state.value,
                onValueChange = {},
                error = state.error,
                enabled = state.enabled,
                visualTransformation = visualTransformation,
                trailingIcon = actionIcon,
                trailingIconContentDescription = when {
                    state.isPassword -> "Показать пароль"
                    state.hasAction -> "Очистить"
                    else -> null
                },
                onTrailingIconClick = if (actionIcon != null) {
                    {}
                } else {
                    null
                }
            )
        }
    }
}

// Интерактивное превью для PrimaryTextField (с показом/скрытием пароля)
@Preview(name = "Интерактивный пароль", showBackground = true)
@Composable
internal fun PrimaryTextFieldInteractivePreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.white900)
                .padding(16.dp)
        ) {
            var text by remember { mutableStateOf("") }
            var isPasswordVisible by remember { mutableStateOf(false) }

            PrimaryTextField(
                title = "Интерактивный пароль",
                placeholder = "Введите пароль...",
                value = text,
                onValueChange = { text = it },
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = rememberVectorPainter(
                    if (isPasswordVisible) {
                        Icons.Filled.VisibilityOff
                    } else {
                        Icons.Filled.Visibility
                    }
                ),
                trailingIconContentDescription = if (isPasswordVisible) {
                    "Скрыть пароль"
                } else {
                    "Показать пароль"
                },
                onTrailingIconClick = { isPasswordVisible = !isPasswordVisible }
            )
        }
    }
}

// Интерактивное превью для поиска с реальной фильтрацией
@Preview(name = "Интерактивный поиск с предложением", showBackground = true)
@Composable
internal fun SearchTextFieldInteractivePreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.white900)
                .padding(16.dp)
        ) {
            var text by remember { mutableStateOf("") }

            val allSuggestions =
                listOf("Telephone", "Television", "Telescope", "Tablet", "Laptop")

            val currentSuggestions = if (text.length > 1) {
                allSuggestions.filter { it.contains(text, ignoreCase = true) }
            } else {
                emptyList()
            }

            SearchTextField(
                value = text,
                title = "Интерактивный поиск",
                onValueChange = { text = it },
                placeholder = "Начни вводить 'Tel'...",
                suggestions = currentSuggestions,
                onSuggestionClick = {
                    text = it
                }
            )
        }
    }
}