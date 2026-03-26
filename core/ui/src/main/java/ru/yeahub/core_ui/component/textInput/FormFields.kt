package ru.yeahub.core_ui.component.textInput

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.core_utils.common.TextOrResource

/**
 * Базовый текстовый компонент проекта YeaHub.
 * Используйте его напрямую, если нужны специфические настройки (иконки, многострочность и т.д.).
 */
@Composable
fun YeaHubTextField(
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
    val isError = error != null

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
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            placeholder = {
                if (placeholder != null) {
                    Text(
                        text = placeholder,
                        style = Theme.typography.body2,
                        color = Theme.colors.black500
                    )
                }
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
            val errorMessage = when (error) {
                is TextOrResource.Resource -> stringResource(error.resource)
                is TextOrResource.Text -> error.text
            }
            Text(
                text = errorMessage,
                color = Theme.colors.red700,
                style = Theme.typography.body1
            )
        }
    }
}

/**
 * Стандартное поле ввода текста с заголовком и поддержкой ошибки (TextOrResource).
 */
@Composable
fun FormTextField(
    title: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier,
    error: TextOrResource? = null,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    YeaHubTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        title = title,
        placeholder = placeholder,
        error = error,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        onFocusChanged = onFocusChanged
    )
}

/**
 * Поле ввода пароля с переключателем видимости и поддержкой ошибки (TextOrResource).
 */
@Composable
fun FormPasswordField(
    title: String,
    placeholder: String,
    value: String,
    isVisible: Boolean,
    onValueChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    error: TextOrResource? = null,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    YeaHubTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        title = title,
        placeholder = placeholder,
        error = error,
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        onFocusChanged = onFocusChanged,
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (isVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = null,
                    tint = Theme.colors.black500
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
internal fun FormFieldsPreview() {
    YeaHubTheme {
        Column(
            modifier = Modifier
                .background(Theme.colors.white900)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormTextField(
                title = "Email",
                placeholder = "example@mail.com",
                value = "",
                onValueChange = {},
                keyboardType = KeyboardType.Email
            )

            FormTextField(
                title = "Email с ошибкой",
                placeholder = "example@mail.com",
                value = "wrong-email",
                onValueChange = {},
                keyboardType = KeyboardType.Email,
                error = TextOrResource.Text("Некорректный формат почты")
            )

            FormPasswordField(
                title = "Пароль",
                placeholder = "Введите пароль",
                value = "password123",
                isVisible = false,
                onValueChange = {},
                onToggleVisibility = {}
            )

            FormPasswordField(
                title = "Пароль (видимый)",
                placeholder = "Введите пароль",
                value = "password123",
                isVisible = true,
                onValueChange = {},
                onToggleVisibility = {},
                error = TextOrResource.Text("Пароль слишком короткий")
            )
        }
    }
}
