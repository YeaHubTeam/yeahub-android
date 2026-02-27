package ru.yeahub.authentication.impl.login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.authentication.impl.R
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.core_ui.theme.colors

private val FIGMA_HORIZONTAL_PADDING = 16.dp
private val FIGMA_TOP_PADDING = 24.dp
private val BLOCK_SPACING = 14.dp

@Composable
fun LoginScreen(
    state: LoginUiState,
    onAction: (Todo) -> Unit,
    modifier: Modifier,
) {
    Scaffold { paddings ->
        Column(
            modifier = modifier
                .padding(paddings)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = FIGMA_HORIZONTAL_PADDING, vertical = FIGMA_TOP_PADDING),
            verticalArrangement = Arrangement.spacedBy(BLOCK_SPACING)
        ) {
            Text(
                text = stringResource(R.string.login_title),
                style = LocalAppTypography.current.head5,
                fontWeight = FontWeight.SemiBold,
                color = colors.black900
            )

            FormTextField(
                title = stringResource(R.string.email_title),
                placeholder = stringResource(R.string.email_placeholder),
                value = state.email,
                errorText = state.emailError,
                keyboardType = KeyboardType.Email,
                onValueChange = { onAction(Todo) },
                modifier = Modifier,
            )

            PasswordBlock(
                password = state.password,
                isVisible = state.isPasswordVisible,
                errorText = state.passwordError,
                onValueChange = { onAction(Todo) },
                onToggleVisibility = { onAction(Todo) },
                onForgotPasswordClick = { onAction(Todo) },
                modifier = Modifier,
            )

            Spacer(modifier = Modifier.height(4.dp))

            PrimaryButtonWithLoading(
                enabled = state.isSubmitEnabled && !state.isLoading,
                loading = state.isLoading,
                onClick = { onAction(Todo) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text(
                    text = stringResource(R.string.login_button),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = stringResource(R.string.social_login_title),
                style = LocalAppTypography.current.body3,
                color = colors.black900
            )

            SocialRow(
                onTelegramClick = { onAction(Todo) },
                modifier = Modifier.fillMaxWidth(),
            )

            BottomSignUpRow(
                onSignUpClick = { onAction(Todo) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun PasswordBlock(
    password: String,
    isVisible: Boolean,
    errorText: String?,
    onValueChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.password_title),
            style = LocalAppTypography.current.body3,
            color = colors.black900
        )

        FormPasswordField(
            placeholder = stringResource(R.string.password_placeholder),
            value = password,
            isVisible = isVisible,
            errorText = errorText,
            onValueChange = onValueChange,
            onToggleVisibility = onToggleVisibility,
            modifier = Modifier,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(R.string.forgot_password),
                style = LocalAppTypography.current.body3,
                color = colors.purple700,
                modifier = Modifier.clickable(onClick = onForgotPasswordClick)
            )
        }
    }
}

@Composable
private fun FormTextField(
    title: String,
    placeholder: String,
    value: String,
    errorText: String?,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = LocalAppTypography.current.body3,
            color = colors.black900
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            isError = errorText != null,
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    color = colors.black400
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colors.black400,
                unfocusedTextColor = colors.black400,
                errorBorderColor = colors.red600,
                errorCursorColor = colors.red600,
            )
        )

        if (errorText != null) {
            Text(
                text = errorText,
                style = LocalAppTypography.current.body1,
                color = colors.red600
            )
        }
    }
}

@Composable
private fun FormPasswordField(
    placeholder: String,
    value: String,
    isVisible: Boolean,
    errorText: String?,
    onValueChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            isError = errorText != null,
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    color = colors.black400
                )
            },
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = onToggleVisibility) {
                    Icon(
                        imageVector = if (isVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = null,
                        tint = colors.black400
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colors.black400,
                unfocusedTextColor = colors.black400,
                errorBorderColor = colors.red600,
                errorCursorColor = colors.red600,
            )
        )

        if (errorText != null) {
            Text(
                text = errorText,
                style = LocalAppTypography.current.body1,
                color = colors.red600
            )
        }
    }
}

@Composable
private fun PrimaryButtonWithLoading(
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    PrimaryButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            content()
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                        .size(18.dp),
                    strokeWidth = 2.dp,
                    color = colors.white900
                )
            }
        }
    }
}

@Composable
private fun SocialRow(
    onTelegramClick: () -> Unit,
    modifier: Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocialIconButton(
            iconRes = R.drawable.ic_telegram,
            contentDescription = "Telegram",
            onClick = onTelegramClick,
            modifier = Modifier,
        )
    }
}

@Composable
private fun SocialIconButton(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    val shape = RoundedCornerShape(50.dp)

    Row(
        modifier = modifier
            .size(44.dp)
            .background(color = Color.Transparent, shape = shape)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = Color.Unspecified
        )
    }
}

@Composable
private fun BottomSignUpRow(
    onSignUpClick: () -> Unit,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .background(
                color = colors.black100.copy(alpha = 0.27f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.no_account),
            style = LocalAppTypography.current.body3,
            color = colors.black900
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.sign_up),
            style = LocalAppTypography.current.body3,
            color = colors.purple700,
            modifier = Modifier.clickable(onClick = onSignUpClick)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview_Empty() {
    YeaHubTheme {
        LoginScreen(
            state = LoginUiState(
                email = "admin@mail.ru",
                password = "123456",
                isPasswordVisible = false,
                emailError = null,
                passwordError = null,
                isSubmitEnabled = true,
                isLoading = false,
            ),
            onAction = {},
            modifier = Modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview_Loading() {
    YeaHubTheme {
        LoginScreen(
            state = LoginUiState(
                email = "admin@mail.ru",
                password = "123456",
                isPasswordVisible = false,
                emailError = null,
                passwordError = null,
                isSubmitEnabled = true,
                isLoading = true,
            ),
            onAction = {},
            modifier = Modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview_Errors() {
    YeaHubTheme {
        LoginScreen(
            state = LoginUiState(
                email = "admin@",
                password = "1",
                isPasswordVisible = false,
                emailError = "Введите корректный email",
                passwordError = "Минимум 6 символов",
                isSubmitEnabled = false,
                isLoading = false,
            ),
            onAction = {},
            modifier = Modifier,
        )
    }
}