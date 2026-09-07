package ru.yeahub.authentication.impl.forgot_password.presentation.change_password.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.ChangePasswordViewModel
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.mapper.ChangePasswordStateMapper
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model.ChangePasswordCommand
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model.ChangePasswordEvent
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model.ChangePasswordState
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model.ChangePasswordRawState
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.PrimaryTextField
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.core_utils.common.observe

@Composable
fun ChangePasswordRoute(
    viewModel: ChangePasswordViewModel,
    onNavigateToProfile: () -> Unit,
    showSnackbar: suspend (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.commands.observe(
        key = viewModel
    ) { command ->
        when (command) {
            is ChangePasswordCommand.NavigateToProfile -> onNavigateToProfile()
            is ChangePasswordCommand.ShowSnackbar -> {
                showSnackbar(command.message.getString(context))
            }
        }
    }

    ChangePasswordScreen(
        state = state,
        onAction = viewModel::onEvent,
    )
}

@Composable
fun ChangePasswordScreen(
    state: ChangePasswordState,
    onAction: (ChangePasswordEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(
            text = stringResource(R.string.change_password_title),
            style = Theme.typography.head5,
            color = Theme.colors.black900,
        )

        Text(
            text = stringResource(R.string.password_rules),
            style = Theme.typography.body3,
            color = Theme.colors.black900,
        )

        state.tokenError?.let { error ->
            Text(
                text = when (error) {
                    is TextOrResource.Resource -> stringResource(error.resource)
                    is TextOrResource.Text -> error.text
                },
                style = Theme.typography.body3,
                color = Theme.colors.red700,
            )
        }

        PasswordTextField(
            title = stringResource(R.string.password_placeholder),
            value = state.password,
            isVisible = state.isPasswordVisible,
            error = state.passwordError,
            enabled = !state.isSubmitting,
            imeAction = ImeAction.Next,
            onValueChange = { onAction(ChangePasswordEvent.OnPasswordChanged(it)) },
            onFocusLost = { onAction(ChangePasswordEvent.OnPasswordFocusLost) },
            onToggleVisibility = { onAction(ChangePasswordEvent.OnTogglePasswordVisible) },
        )

        PasswordTextField(
            title = stringResource(R.string.repeat_password),
            value = state.repeatedPassword,
            isVisible = state.isRepeatedPasswordVisible,
            error = state.repeatedPasswordError,
            enabled = !state.isSubmitting,
            imeAction = ImeAction.Done,
            onValueChange = { onAction(ChangePasswordEvent.OnRepeatedPasswordChanged(it)) },
            onFocusLost = { onAction(ChangePasswordEvent.OnRepeatedPasswordFocusLost) },
            onToggleVisibility = { onAction(ChangePasswordEvent.OnToggleRepeatedPasswordVisible) },
            keyboardActions = KeyboardActions(
                onDone = { onAction(ChangePasswordEvent.OnSaveClicked) },
            ),
        )

        PrimaryButton(
            onClick = { onAction(ChangePasswordEvent.OnSaveClicked) },
            enabled = state.isSubmitEnabled && !state.isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
        ) {
            if (state.isSubmitting) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    color = Theme.colors.white900,
                    modifier = Modifier.height(18.dp),
                )
            } else {
                Text(
                    text = stringResource(R.string.save_new_password),
                    style = Theme.typography.body4,
                )
            }
        }
    }
}

@Composable
private fun PasswordTextField(
    title: String,
    value: String,
    isVisible: Boolean,
    error: TextOrResource?,
    enabled: Boolean,
    imeAction: ImeAction,
    onValueChange: (String) -> Unit,
    onFocusLost: () -> Unit,
    onToggleVisibility: () -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    PrimaryTextField(
        value = value,
        onValueChange = onValueChange,
        title = title,
        placeholder = stringResource(R.string.password_title),
        error = error,
        enabled = enabled,
        visualTransformation = if (isVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction,
        ),
        keyboardActions = keyboardActions,
        trailingIcon = rememberVectorPainter(
            image = if (isVisible) {
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility
            },
        ),
        trailingIconContentDescription = if (isVisible) {
            stringResource(R.string.login_password_visibility_hide)
        } else {
            stringResource(R.string.login_password_visibility_show)
        },
        onTrailingIconClick = onToggleVisibility,
        onFocusChanged = { isFocused ->
            if (!isFocused) {
                onFocusLost()
            }
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview(showBackground = true, name = "Change Password")
@Composable
fun ChangePasswordScreenPreview_interactivePreview() {
    val mapper = remember { ChangePasswordStateMapper() }
    var previewInput by remember {
        mutableStateOf(
            ChangePasswordRawState(
                password = "Password123!",
                repeatedPassword = "Password123!",
                isPasswordVisible = true,
                isRepeatedPasswordVisible = true,
                isPasswordTouched = false,
                isRepeatedPasswordTouched = false,
                isValidationRequested = false,
                isSubmitting = false,
                passwordServerError = null,
            )
        )
    }
    var previewState by remember {
        mutableStateOf(
            mapper.mapToScreenState(rawState = previewInput)
        )
    }

    YeaHubTheme {
        ChangePasswordScreen(
            state = previewState,
            onAction = { action ->
                previewInput = when (action) {
                    is ChangePasswordEvent.OnPasswordChanged -> {
                        previewInput.copy(
                            password = action.value,
                            passwordServerError = null,
                        )
                    }

                    is ChangePasswordEvent.OnRepeatedPasswordChanged -> {
                        previewInput.copy(
                            repeatedPassword = action.value,
                            passwordServerError = null,
                        )
                    }

                    ChangePasswordEvent.OnTogglePasswordVisible -> {
                        previewInput.copy(isPasswordVisible = !previewInput.isPasswordVisible)
                    }

                    ChangePasswordEvent.OnToggleRepeatedPasswordVisible -> {
                        previewInput.copy(
                            isRepeatedPasswordVisible = !previewInput.isRepeatedPasswordVisible
                        )
                    }

                    ChangePasswordEvent.OnPasswordFocusLost -> {
                        previewInput.copy(isPasswordTouched = true)
                    }

                    ChangePasswordEvent.OnRepeatedPasswordFocusLost -> {
                        previewInput.copy(isRepeatedPasswordTouched = true)
                    }

                    ChangePasswordEvent.OnSaveClicked -> {
                        previewInput.copy(
                            isPasswordTouched = true,
                            isRepeatedPasswordTouched = true,
                            isValidationRequested = true,
                        )
                    }
                }
                previewState = mapper.mapToScreenState(rawState = previewInput)
            },
        )
    }
}

@Preview(showBackground = true, name = "Change Password - incorrect password")
@Composable
fun ChangePasswordScreenPreviewIncorrectPassword() {
    YeaHubTheme {
        ChangePasswordScreen(
            state = ChangePasswordState(
                password = "word",
                repeatedPassword = "word",
                isPasswordVisible = true,
                isRepeatedPasswordVisible = true,
                passwordError = TextOrResource.Resource(R.string.password_error_too_short),
                repeatedPasswordError = null,
                tokenError = null,
                isSubmitEnabled = false,
                isSubmitting = false,
            ),
            onAction = {},
        )
    }
}

@Preview(showBackground = true, name = "Change Password - passwords not matching")
@Composable
fun ChangePasswordScreenPreviewIncorrectPasswordNotMatching() {
    YeaHubTheme {
        ChangePasswordScreen(
            state = ChangePasswordState(
                password = "Password88",
                repeatedPassword = "Password",
                isPasswordVisible = true,
                isRepeatedPasswordVisible = true,
                passwordError = null,
                repeatedPasswordError = TextOrResource.Resource(R.string.passwords_do_not_match),
                tokenError = null,
                isSubmitEnabled = false,
                isSubmitting = false,
            ),
            onAction = {},
        )
    }
}

@Preview(showBackground = true, name = "Change Password - blank reset token")
@Composable
fun ChangePasswordScreenPreviewBlankResetToken() {
    YeaHubTheme {
        ChangePasswordScreen(
            state = ChangePasswordState(
                password = "",
                repeatedPassword = "",
                isPasswordVisible = true,
                isRepeatedPasswordVisible = true,
                passwordError = null,
                repeatedPasswordError = null,
                tokenError = TextOrResource.Resource(R.string.reset_link_invalid),
                isSubmitEnabled = false,
                isSubmitting = false,
            ),
            onAction = {},
        )
    }
}