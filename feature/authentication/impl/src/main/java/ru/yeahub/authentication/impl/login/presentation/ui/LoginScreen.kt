package ru.yeahub.authentication.impl.login.presentation.ui

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.login.presentation.model.LoginAction
import ru.yeahub.authentication.impl.login.presentation.model.LoginCommand
import ru.yeahub.authentication.impl.login.presentation.model.LoginState
import ru.yeahub.authentication.impl.login.presentation.preview.StandardPreviewHeight
import ru.yeahub.authentication.impl.login.presentation.preview.StandardPreviewWidth
import ru.yeahub.authentication.impl.login.presentation.preview.StandardScreenSizePreview
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.PrimaryTextField
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.core_ui.theme.colors
import ru.yeahub.core_utils.common.TextOrResource

private val FigmaHorizontalPadding = 16.dp
private val FigmaTopPadding = 24.dp
private val BlockSpacing = 14.dp

/**
 * Корневой экран логина:
 * - показывает UI
 * - подключает обработку команд
 */
@Composable
fun LoginScreen(
    state: LoginState,
    commands: SharedFlow<LoginCommand>,
    onAction: (LoginAction) -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    modifier: Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    HandleCommands(
        commands = commands,
        snackbarHostState = snackbarHostState,
        onNavigateToMain = onNavigateToMain,
        onNavigateToSignUp = onNavigateToSignUp,
        onNavigateToForgotPassword = onNavigateToForgotPassword,
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            )
        },
    ) { paddings ->
        LoginContent(
            state = state,
            onAction = onAction,
            modifier = modifier.padding(paddings),
        )
    }
}

/**
 * Обрабатывает одноразовые команды экрана.
 */
@Composable
private fun HandleCommands(
    commands: SharedFlow<LoginCommand>,
    snackbarHostState: SnackbarHostState,
    onNavigateToMain: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        commands.collect { command ->
            when (command) {
                is LoginCommand.NavigateToMain -> onNavigateToMain()
                is LoginCommand.NavigateToSignUp -> onNavigateToSignUp()
                is LoginCommand.NavigateToForgotPassword -> onNavigateToForgotPassword()

                is LoginCommand.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = command.message.getString(context),
                    )
                }
            }
        }
    }
}

/**
 * Отображает основной контент экрана.
 */
@Composable
private fun LoginContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier,
) {
    LoginFormContent(
        state = state,
        onAction = onAction,
        modifier = modifier,
    )
}

/**
 * Основной контент формы логина.
 */
@Composable
private fun LoginFormContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier,
) {
    var wasEmailFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = FigmaHorizontalPadding,
                vertical = FigmaTopPadding,
            ),
        verticalArrangement = Arrangement.spacedBy(BlockSpacing),
    ) {
        Text(
            text = stringResource(R.string.login_title),
            style = LocalAppTypography.current.head5,
            fontWeight = FontWeight.SemiBold,
            color = colors.black900,
        )

        PrimaryTextField(
            value = state.email,
            onValueChange = { value ->
                onAction(LoginAction.OnEmailChanged(value))
            },
            title = stringResource(R.string.email_title),
            placeholder = stringResource(R.string.email_placeholder),
            error = state.emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        wasEmailFocused = true
                    }

                    if (wasEmailFocused && focusState.isFocused.not()) {
                        onAction(LoginAction.OnEmailFocusLost)
                    }
                },
        )

        PasswordBlock(
            password = state.password,
            isVisible = state.isPasswordVisible,
            error = state.passwordError,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(
            modifier = Modifier.height(4.dp),
        )

        PrimaryButtonWithLoading(
            enabled = state.isSubmitEnabled && state.isSubmitting.not(),
            loading = state.isSubmitting,
            onClick = {
                onAction(LoginAction.OnLoginClick)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            content = {
                Text(
                    text = stringResource(R.string.login_button),
                    fontWeight = FontWeight.SemiBold,
                )
            },
        )

        Spacer(
            modifier = Modifier.size(72.dp),
        )

        BottomSignUpRow(
            onSignUpClick = {
                onAction(LoginAction.OnSignUpClick)
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * Блок поля пароля и кнопки восстановления.
 */
@Composable
private fun PasswordBlock(
    password: String,
    isVisible: Boolean,
    error: TextOrResource?,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier,
) {
    var wasPasswordFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        PrimaryTextField(
            value = password,
            onValueChange = { value ->
                onAction(LoginAction.OnPasswordChanged(value))
            },
            title = stringResource(R.string.password_title),
            placeholder = stringResource(R.string.password_placeholder),
            error = error,
            visualTransformation = if (isVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
            ),
            trailingIcon = rememberVectorPainter(
                image = if (isVisible) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                },
            ),
            trailingIconContentDescription = stringResource(
                id = if (isVisible) {
                    R.string.login_password_visibility_hide
                } else {
                    R.string.login_password_visibility_show
                },
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        wasPasswordFocused = true
                    }

                    if (wasPasswordFocused && focusState.isFocused.not()) {
                        onAction(LoginAction.OnPasswordFocusLost)
                    }
                },
            onTrailingIconClick = {
                onAction(LoginAction.OnTogglePasswordVisible)
            },
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Text(
                text = stringResource(R.string.forgot_password),
                style = LocalAppTypography.current.body3,
                color = colors.purple700,
                modifier = Modifier.clickable(
                    onClick = {
                        onAction(LoginAction.OnForgotPasswordClick)
                    },
                ),
            )
        }
    }
}

/**
 * Кнопка входа с индикатором загрузки.
 */
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
            contentAlignment = Alignment.Center,
        ) {
            content()

            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                        .size(18.dp),
                    strokeWidth = 2.dp,
                    color = colors.white900,
                )
            }
        }
    }
}

/**
 * Нижний блок перехода к регистрации.
 */
@Composable
private fun BottomSignUpRow(
    onSignUpClick: () -> Unit,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .background(
                color = colors.black100.copy(alpha = 0.27f),
                shape = RoundedCornerShape(12.dp),
            )
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.no_account),
            style = LocalAppTypography.current.body3,
            color = colors.black900,
        )

        Spacer(
            modifier = Modifier.weight(1f),
        )

        Text(
            text = stringResource(R.string.sign_up),
            style = LocalAppTypography.current.body3,
            color = colors.purple700,
            modifier = Modifier.clickable(
                onClick = onSignUpClick,
            ),
        )
    }
}

/**
 * Набор состояний для dynamic preview логина.
 */
class LoginStatePreviewProvider : PreviewParameterProvider<LoginState> {

    override val values: Sequence<LoginState> = sequenceOf(
        LoginState(
            email = "",
            password = "",
            isPasswordVisible = false,
            emailError = null,
            passwordError = null,
            isSubmitEnabled = false,
            isSubmitting = false,
        ),
        LoginState(
            email = "user@example.com",
            password = "qwerty",
            isPasswordVisible = false,
            emailError = null,
            passwordError = null,
            isSubmitEnabled = true,
            isSubmitting = false,
        ),
        LoginState(
            email = "user@example.com",
            password = "qwerty",
            isPasswordVisible = false,
            emailError = null,
            passwordError = null,
            isSubmitEnabled = true,
            isSubmitting = true,
        ),
        LoginState(
            email = "invalid-email",
            password = "",
            isPasswordVisible = false,
            emailError = TextOrResource.Resource(R.string.login_email_invalid),
            passwordError = TextOrResource.Resource(R.string.login_password_empty),
            isSubmitEnabled = false,
            isSubmitting = false,
        ),
        LoginState(
            email = "user@example.com",
            password = "123456",
            isPasswordVisible = false,
            emailError = TextOrResource.Resource(R.string.login_invalid_credentials),
            passwordError = null,
            isSubmitEnabled = true,
            isSubmitting = false,
        ),
    )
}

/**
 * Dynamic preview экрана логина.
 */
@Preview(
    name = "Dynamic Login Preview",
    showBackground = true,
)
@Composable
fun DynamicLoginPreview(
    @PreviewParameter(LoginStatePreviewProvider::class) state: LoginState,
) {
    YeaHubTheme {
        StandardScreenSizePreview(
            modifier = Modifier,
            width = StandardPreviewWidth,
            height = StandardPreviewHeight,
            content = {
                LoginScreen(
                    state = state,
                    commands = MutableSharedFlow(),
                    onAction = {},
                    onNavigateToMain = {},
                    onNavigateToSignUp = {},
                    onNavigateToForgotPassword = {},
                    modifier = Modifier,
                )
            },
        )
    }
}

/**
 * Static preview пустой формы.
 */
@Preview(
    name = "Login Initial",
    showBackground = true,
)
@Composable
fun LoginScreenPreviewInitial() {
    YeaHubTheme {
        StandardScreenSizePreview(
            modifier = Modifier,
            width = StandardPreviewWidth,
            height = StandardPreviewHeight,
            content = {
                LoginScreen(
                    state = LoginState(
                        email = "",
                        password = "",
                        isPasswordVisible = false,
                        emailError = null,
                        passwordError = null,
                        isSubmitEnabled = false,
                        isSubmitting = false,
                    ),
                    commands = MutableSharedFlow(),
                    onAction = {},
                    onNavigateToMain = {},
                    onNavigateToSignUp = {},
                    onNavigateToForgotPassword = {},
                    modifier = Modifier,
                )
            },
        )
    }
}

/**
 * Static preview заполненной формы.
 */
@Preview(
    name = "Login Editing",
    showBackground = true,
)
@Composable
fun LoginScreenPreviewEditing() {
    YeaHubTheme {
        StandardScreenSizePreview(
            modifier = Modifier,
            width = StandardPreviewWidth,
            height = StandardPreviewHeight,
            content = {
                LoginScreen(
                    state = LoginState(
                        email = "admin@mail.ru",
                        password = "qwerty",
                        isPasswordVisible = false,
                        emailError = null,
                        passwordError = null,
                        isSubmitEnabled = true,
                        isSubmitting = false,
                    ),
                    commands = MutableSharedFlow(),
                    onAction = {},
                    onNavigateToMain = {},
                    onNavigateToSignUp = {},
                    onNavigateToForgotPassword = {},
                    modifier = Modifier,
                )
            },
        )
    }
}

/**
 * Static preview состояния загрузки.
 */
@Preview(
    name = "Login Loading",
    showBackground = true,
)
@Composable
fun LoginScreenPreviewLoading() {
    YeaHubTheme {
        StandardScreenSizePreview(
            modifier = Modifier,
            width = StandardPreviewWidth,
            height = StandardPreviewHeight,
            content = {
                LoginScreen(
                    state = LoginState(
                        email = "admin@mail.ru",
                        password = "qwerty",
                        isPasswordVisible = false,
                        emailError = null,
                        passwordError = null,
                        isSubmitEnabled = true,
                        isSubmitting = true,
                    ),
                    commands = MutableSharedFlow(),
                    onAction = {},
                    onNavigateToMain = {},
                    onNavigateToSignUp = {},
                    onNavigateToForgotPassword = {},
                    modifier = Modifier,
                )
            },
        )
    }
}

/**
 * Static preview локальных ошибок после submit.
 */
@Preview(
    name = "Login Validation",
    showBackground = true,
)
@Composable
fun LoginScreenPreviewValidation() {
    YeaHubTheme {
        StandardScreenSizePreview(
            modifier = Modifier,
            width = StandardPreviewWidth,
            height = StandardPreviewHeight,
            content = {
                LoginScreen(
                    state = LoginState(
                        email = "admin@",
                        password = "",
                        isPasswordVisible = false,
                        emailError = TextOrResource.Resource(R.string.login_email_invalid),
                        passwordError = TextOrResource.Resource(R.string.login_password_empty),
                        isSubmitEnabled = false,
                        isSubmitting = false,
                    ),
                    commands = MutableSharedFlow(),
                    onAction = {},
                    onNavigateToMain = {},
                    onNavigateToSignUp = {},
                    onNavigateToForgotPassword = {},
                    modifier = Modifier,
                )
            },
        )
    }
}

/**
 * Static preview серверной ошибки логина.
 */
@Preview(
    name = "Login Server Error",
    showBackground = true,
)
@Composable
fun LoginScreenPreviewServerError() {
    YeaHubTheme {
        StandardScreenSizePreview(
            modifier = Modifier,
            width = StandardPreviewWidth,
            height = StandardPreviewHeight,
            content = {
                LoginScreen(
                    state = LoginState(
                        email = "user@example.com",
                        password = "123456",
                        isPasswordVisible = false,
                        emailError = TextOrResource.Resource(R.string.login_invalid_credentials),
                        passwordError = null,
                        isSubmitEnabled = true,
                        isSubmitting = false,
                    ),
                    commands = MutableSharedFlow(),
                    onAction = {},
                    onNavigateToMain = {},
                    onNavigateToSignUp = {},
                    onNavigateToForgotPassword = {},
                    modifier = Modifier,
                )
            },
        )
    }
}