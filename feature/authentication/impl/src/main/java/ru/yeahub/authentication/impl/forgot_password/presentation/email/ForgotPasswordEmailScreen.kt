@file:OptIn(ExperimentalMaterial3Api::class)

package ru.yeahub.authentication.impl.forgot_password.presentation.email

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.forgot_password.presentation.email.model.ForgotPasswordEmailAction
import ru.yeahub.authentication.impl.forgot_password.presentation.email.model.ForgotPasswordEmailState
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.PrimaryTextField
import ru.yeahub.core_ui.component.YeahubButtonColors
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.core_ui.theme.colors
import ru.yeahub.core_utils.common.TextOrResource

@Composable
fun ForgotPasswordEmailScreen(
    state: ForgotPasswordEmailState,
    onAction: (ForgotPasswordEmailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isSuccessDialogVisible) {
        InstructionsSentDialog(
            onDismiss = { onAction(ForgotPasswordEmailAction.OnDismissSuccessDialog) },
            onResend = { onAction(ForgotPasswordEmailAction.OnResendClick) },
            secondsLeft = state.cooldownSecondsLeft
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp)
    ) {
        Text(
            text = stringResource(R.string.forgot_password),
            style = Theme.typography.head5,
            color = Theme.colors.black900
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.enter_email_instruction),
            style = Theme.typography.body2Accent,
            color = Theme.colors.black500
        )

        Spacer(Modifier.height(96.dp))

        PrimaryTextField(
            value = state.email,
            onValueChange = { onAction(ForgotPasswordEmailAction.OnEmailChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.email_title),
            placeholder = stringResource(R.string.email_placeholder),
            error = state.emailError,
            enabled = !state.isSubmitting,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onFocusChanged = { isFocused ->
                if (!isFocused) {
                    onAction(ForgotPasswordEmailAction.OnEmailFocusLost)
                }
            }
        )

        Spacer(Modifier.height(20.dp))

        PrimaryButton(
            onClick = { onAction(ForgotPasswordEmailAction.OnSubmitClick) },
            enabled = state.isSubmitEnabled && !state.isSubmitting && !state.isCooldownActive,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = YeahubButtonColors(
                contentColor = Theme.colors.white900,
                containerColor = Theme.colors.purple700,
                disabledContentColor = Theme.colors.black200,
                disabledContainerColor = Theme.colors.black50
            )
        ) {
            if (state.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(18.dp),
                    strokeWidth = 2.dp,
                    color = colors.white900,
                )
            }

            Text(
                text = if (!state.isCooldownActive) {
                    stringResource(R.string.send_button)
                } else {
                    stringResource(
                        R.string.send_again_in_seconds,
                        state.cooldownSecondsLeft
                    )
                },
                style = Theme.typography.body4
            )
        }
    }
}

@Composable
private fun ForgotPasswordEmailScreenPreviewContent(
    state: ForgotPasswordEmailState,
) {
    YeaHubTheme {
        ForgotPasswordEmailScreen(
            state = state,
            onAction = {},
        )
    }
}

@Preview(showBackground = true, name = "Initial State")
@Composable
fun ForgotPasswordEmailScreenPreview_Initial() {
    YeaHubTheme {
        ForgotPasswordEmailScreenPreviewContent(
            state = ForgotPasswordEmailState(
                email = "",
                emailError = null,
                isSubmitEnabled = false,
                isSubmitting = false,
                isSuccessDialogVisible = false,
                cooldownSecondsLeft = 0,
            ),
        )
    }
}

@Preview(showBackground = true, name = "Valid Email")
@Composable
fun ForgotPasswordEmailScreenPreview_Valid() {
    YeaHubTheme {
        ForgotPasswordEmailScreenPreviewContent(
            state = ForgotPasswordEmailState(
                email = "user@example.com",
                emailError = null,
                isSubmitEnabled = true,
                isSubmitting = false,
                isSuccessDialogVisible = false,
                cooldownSecondsLeft = 0,
            ),
        )
    }
}

@Preview(showBackground = true, name = "Invalid Email")
@Composable
fun ForgotPasswordEmailScreenPreview_Invalid() {
    YeaHubTheme {
        ForgotPasswordEmailScreenPreviewContent(
            state = ForgotPasswordEmailState(
                email = "invalid-email",
                emailError = TextOrResource.Resource(R.string.login_email_invalid),
                isSubmitEnabled = false,
                isSubmitting = false,
                isSuccessDialogVisible = false,
                cooldownSecondsLeft = 0,
            ),
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
fun ForgotPasswordEmailScreenPreview_Loading() {
    YeaHubTheme {
        ForgotPasswordEmailScreenPreviewContent(
            state = ForgotPasswordEmailState(
                email = "user@example.com",
                emailError = null,
                isSubmitEnabled = true,
                isSubmitting = true,
                isSuccessDialogVisible = false,
                cooldownSecondsLeft = 0,
                // TODO: какое точно должно быть тут сек?
            ),
        )
    }
}

@Preview(showBackground = true, name = "Cooldown 49 Seconds")
@Composable
fun ForgotPasswordEmailScreenPreview_Cooldown49Seconds_SuccessDialogVisible() {
    YeaHubTheme {
        ForgotPasswordEmailScreenPreviewContent(
            state = ForgotPasswordEmailState(
                email = "user@example.com",
                emailError = null,
                isSubmitEnabled = false,
                isSubmitting = false,
                isSuccessDialogVisible = true,
                cooldownSecondsLeft = 49,
            ),
        )
    }
}

@Preview(showBackground = true, name = "Cooldown 49 Seconds")
@Composable
fun ForgotPasswordEmailScreenPreview_Cooldown49Seconds() {
    YeaHubTheme {
        ForgotPasswordEmailScreenPreviewContent(
            state = ForgotPasswordEmailState(
                email = "user@example.com",
                emailError = null,
                isSubmitEnabled = false,
                isSubmitting = false,
                isSuccessDialogVisible = false,
                cooldownSecondsLeft = 49,
            ),
        )
    }
}