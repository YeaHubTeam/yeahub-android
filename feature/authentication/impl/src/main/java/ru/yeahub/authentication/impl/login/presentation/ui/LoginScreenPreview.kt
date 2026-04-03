package ru.yeahub.authentication.impl.login.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.login.presentation.model.LoginFormState
import ru.yeahub.authentication.impl.login.presentation.model.LoginState
import ru.yeahub.authentication.impl.login.presentation.preview.StandardPreviewHeight
import ru.yeahub.authentication.impl.login.presentation.preview.StandardPreviewWidth
import ru.yeahub.authentication.impl.login.presentation.preview.StandardScreenSizePreview
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.core_utils.common.TextOrResource

/**
 * Набор состояний для dynamic preview логина.
 */
class LoginStatePreviewProvider : PreviewParameterProvider<LoginState> {

    override val values: Sequence<LoginState> = sequenceOf(
        LoginState.Content(
            formState = LoginFormState(
                email = "",
                password = "",
                isPasswordVisible = false,
                emailError = null,
                passwordError = null,
                isSubmitEnabled = false,
            ),
        ),
        LoginState.Content(
            formState = LoginFormState(
                email = "user@example.com",
                password = "qwerty",
                isPasswordVisible = false,
                emailError = null,
                passwordError = null,
                isSubmitEnabled = true,
            ),
        ),
        LoginState.Loading(
            formState = LoginFormState(
                email = "user@example.com",
                password = "qwerty",
                isPasswordVisible = false,
                emailError = null,
                passwordError = null,
                isSubmitEnabled = true,
            ),
        ),
        LoginState.Content(
            formState = LoginFormState(
                email = "invalid-email",
                password = "",
                isPasswordVisible = false,
                emailError = TextOrResource.Resource(R.string.login_email_invalid),
                passwordError = TextOrResource.Resource(R.string.login_password_empty),
                isSubmitEnabled = false,
            ),
        ),
        LoginState.Content(
            formState = LoginFormState(
                email = "user@example.com",
                password = "123456",
                isPasswordVisible = false,
                emailError = TextOrResource.Resource(R.string.login_invalid_credentials),
                passwordError = null,
                isSubmitEnabled = true,
            ),
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
    name = "Login Empty",
    showBackground = true,
)
@Composable
fun LoginScreenPreviewEmpty() {
    YeaHubTheme {
        StandardScreenSizePreview(
            modifier = Modifier,
            width = StandardPreviewWidth,
            height = StandardPreviewHeight,
            content = {
                LoginScreen(
                    state = LoginState.Content(
                        formState = LoginFormState(
                            email = "",
                            password = "",
                            isPasswordVisible = false,
                            emailError = null,
                            passwordError = null,
                            isSubmitEnabled = false,
                        ),
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
    name = "Login Filled",
    showBackground = true,
)
@Composable
fun LoginScreenPreviewFilled() {
    YeaHubTheme {
        StandardScreenSizePreview(
            modifier = Modifier,
            width = StandardPreviewWidth,
            height = StandardPreviewHeight,
            content = {
                LoginScreen(
                    state = LoginState.Content(
                        formState = LoginFormState(
                            email = "admin@mail.ru",
                            password = "qwerty",
                            isPasswordVisible = false,
                            emailError = null,
                            passwordError = null,
                            isSubmitEnabled = true,
                        ),
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
                    state = LoginState.Loading(
                        formState = LoginFormState(
                            email = "admin@mail.ru",
                            password = "qwerty",
                            isPasswordVisible = false,
                            emailError = null,
                            passwordError = null,
                            isSubmitEnabled = true,
                        ),
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
    name = "Login Local Errors After Submit",
    showBackground = true,
)
@Composable
fun LoginScreenPreviewLocalErrorsAfterSubmit() {
    YeaHubTheme {
        StandardScreenSizePreview(
            modifier = Modifier,
            width = StandardPreviewWidth,
            height = StandardPreviewHeight,
            content = {
                LoginScreen(
                    state = LoginState.Content(
                        formState = LoginFormState(
                            email = "admin@",
                            password = "",
                            isPasswordVisible = false,
                            emailError = TextOrResource.Resource(R.string.login_email_invalid),
                            passwordError = TextOrResource.Resource(R.string.login_password_empty),
                            isSubmitEnabled = false,
                        ),
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
                    state = LoginState.Content(
                        formState = LoginFormState(
                            email = "user@example.com",
                            password = "123456",
                            isPasswordVisible = false,
                            emailError = TextOrResource.Resource(R.string.login_invalid_credentials),
                            passwordError = null,
                            isSubmitEnabled = true,
                        ),
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