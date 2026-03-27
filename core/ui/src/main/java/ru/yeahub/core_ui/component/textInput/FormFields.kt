package ru.yeahub.core_ui.component.textInput

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.core_utils.common.TextOrResource

/**
 * Базовый компонент
 * Использовать напрямую только если нужна сложная кастомная верстка внутри поля
 */
@Composable
internal fun CoreTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String? = null,
    error: TextOrResource? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (title != null) {
            Text(
                text = title,
                style = Theme.typography.body2Accent,
                color = Theme.colors.black900
            )
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { onFocusChanged(it.isFocused) },
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            isError = error != null,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            placeholder = placeholder?.let {
                { Text(text = it, style = Theme.typography.body2, color = Theme.colors.black500) }
            },
            trailingIcon = trailingIcon,
            singleLine = singleLine,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Theme.colors.black900,
                unfocusedTextColor = Theme.colors.black900,
                focusedBorderColor = Theme.colors.purple700,
                unfocusedBorderColor = Theme.colors.black100,
                errorBorderColor = Theme.colors.red700,
                disabledBorderColor = Theme.colors.black100,
                disabledTextColor = Theme.colors.black500
            )
        )

        if (error != null) {
            Text(
                text = when (error) {
                    is TextOrResource.Resource -> stringResource(error.resource)
                    is TextOrResource.Text -> error.text
                },
                color = Theme.colors.red700,
                style = Theme.typography.body1
            )
        }
    }
}

/**
 * Универсальное поле ввода для любых задач
 * Не имеет привязки к бизнес-логике (пароли, поиск и т.д.)
 */
@Composable
fun PrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String? = null,
    error: TextOrResource? = null,
    actionIcon: Painter? = null,
    actionContentDescription: String? = null,
    onActionClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
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
        onFocusChanged = onFocusChanged,
        trailingIcon = if (actionIcon != null && onActionClick != null) {
            {
                IconButton(onClick = onActionClick, enabled = enabled) {
                    Icon(
                        painter = actionIcon,
                        contentDescription = actionContentDescription,
                        tint = Theme.colors.black500
                    )
                }
            }
        } else {
            null
        }
    )
}

@Preview(name = "1. Пустое поле ввода", showBackground = true)
@Composable
internal fun PrimaryTextFieldDefaultPreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.white900)
                .padding(16.dp)
        ) {
            PrimaryTextField(
                title = "Email",
                placeholder = "example@mail.com",
                value = "",
                onValueChange = {}
            )
        }
    }
}

@Preview(name = "2. Поиск с очисткой поля ввода", showBackground = true)
@Composable
internal fun PrimaryTextFieldFilledPreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.white900)
                .padding(16.dp)
        ) {
            PrimaryTextField(
                title = "Поиск",
                value = "Текст запроса",
                onValueChange = {},
                actionIcon = rememberVectorPainter(Icons.Filled.Clear),
                actionContentDescription = "Очистить",
                onActionClick = {}
            )
        }
    }
}

@Preview(name = "3. Пароль скрыт", showBackground = true)
@Composable
internal fun PrimaryTextFieldPasswordPreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.white900)
                .padding(16.dp)
        ) {
            // Хардкодим стейты для превью, никаких remember
            PrimaryTextField(
                title = "Пароль",
                value = "password123",
                onValueChange = {},
                visualTransformation = PasswordVisualTransformation(),
                actionIcon = rememberVectorPainter(Icons.Filled.Visibility),
                onActionClick = {}
            )
        }
    }
}

@Preview(name = "4. Пароль виден и ошибка", showBackground = true)
@Composable
internal fun PrimaryTextFieldErrorPreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.white900)
                .padding(16.dp)
        ) {
            PrimaryTextField(
                title = "Пароль",
                value = "123",
                onValueChange = {},
                error = TextOrResource.Text("Минимальная длина 8 символов"),
                actionIcon = rememberVectorPainter(Icons.Filled.VisibilityOff),
                onActionClick = {}
            )
        }
    }
}

@Preview(name = "5. Заблокировано", showBackground = true)
@Composable
internal fun PrimaryTextFieldDisabledPreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.white900)
                .padding(16.dp)
        ) {
            PrimaryTextField(
                title = "Недоступно",
                value = "Текст нельзя изменить",
                onValueChange = {},
                enabled = false
            )
        }
    }
}

@Preview(name = "6. Минималистично", showBackground = true)
@Composable
internal fun PrimaryTextFieldMinimalPreview() {
    YeaHubTheme {
        Box(
            modifier = Modifier
                .background(Theme.colors.white900)
                .padding(16.dp)
        ) {
            PrimaryTextField(
                // Не передаем title и placeholder, проверяем верстку без них
                value = "Только поле ввода",
                onValueChange = {}
            )
        }
    }
}