package ru.yeahub.authentication.impl.forgotpassword.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.forgotpassword.presentation.model.ForgotPasswordAction
import ru.yeahub.authentication.impl.forgotpassword.presentation.model.ForgotPasswordCommand
import ru.yeahub.authentication.impl.forgotpassword.presentation.model.ForgotPasswordState
import ru.yeahub.authentication.impl.forgotpassword.presentation.viewmodel.ForgotPasswordViewModel
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.PrimaryTextField
import ru.yeahub.core_ui.theme.LocalAppTypography
import ru.yeahub.core_ui.theme.YeaHubTheme
import ru.yeahub.core_ui.theme.colors

internal val dialogShape = RoundedCornerShape(16.dp)

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = koinViewModel(),
    onBackToSignIn: () -> Unit,
    modifier: Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.commands.collect { command ->
            when (command) {
                ForgotPasswordCommand.NavigateToSignIn -> onBackToSignIn()
            }
        }
    }

    ForgotPasswordContent(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
fun ForgotPasswordContent(
    state: ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit,
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(99.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        ForgotPasswordHeader()

        if (state.isSuccessDialogVisible) {
            ForgotPasswordSuccessDialog(
                onAction = onAction,
                modifier = modifier
            )
        } else {
            EmailInputBlock(
                state = state,
                onAction = onAction
            )
        }
    }
}

@Composable
fun ForgotPasswordHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.forgot_password),
            style = LocalAppTypography.current.head5,
            color = colors.black900
        )
        Text(
            text = stringResource(R.string.enter_email_instruction),
            style = LocalAppTypography.current.body2Accent,
            color = colors.black400
        )
    }
}

@Composable
fun EmailInputBlock(
    state: ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PrimaryTextField(
            title = stringResource(R.string.email_title),
            placeholder = stringResource(R.string.email_placeholder),
            value = state.email,
            onValueChange = { onAction(ForgotPasswordAction.OnEmailChanged(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onFocusChanged = {}
        )

        PrimaryButtonWithLoading(
            onClick = { onAction(ForgotPasswordAction.OnButtonClick) },
            enabled = state.isSubmitEnabled,
            modifier = Modifier.fillMaxWidth(),
            loading = state.isSubmitting,
            content = {
                Text(
                    text = stringResource(R.string.send_button),
                    fontWeight = FontWeight.SemiBold,
                )
            }
        )
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

@Preview(name = "Start state", showBackground = true)
@Composable
fun ForgotPasswordContentStartPreview() {
    YeaHubTheme {
        ForgotPasswordContent(
            modifier = Modifier,
            state = ForgotPasswordState(
                email = "",
                isSubmitEnabled = false,
                isSuccessDialogVisible = false,
                isSubmitting = false
            ),
            onAction = { },
        )
    }
}

@Preview(name = "Invalid email", showBackground = true)
@Composable
fun ForgotPasswordContentInvalidEmailPreview() {
    YeaHubTheme {
        ForgotPasswordContent(
            modifier = Modifier,
            state = ForgotPasswordState(
                email = "АБВГ123",
                isSubmitEnabled = false,
                isSuccessDialogVisible = false,
                isSubmitting = false
            ),
            onAction = { },
        )
    }
}

@Preview(name = "Correct email", showBackground = true)
@Composable
fun ForgotPasswordContentCorrectEmailPreview() {
    YeaHubTheme {
        ForgotPasswordContent(
            modifier = Modifier,
            state = ForgotPasswordState(
                email = "develop@yeahub.ru",
                isSubmitEnabled = true,
                isSuccessDialogVisible = false,
                isSubmitting = true
            ),
            onAction = { },
        )
    }
}

@Preview(name = "Mail sent", showBackground = true)
@Composable
fun ForgotPasswordContentDialogPreview() {
    YeaHubTheme {
        ForgotPasswordContent(
            modifier = Modifier,
            state = ForgotPasswordState(
                email = "",
                isSubmitEnabled = false,
                isSuccessDialogVisible = true,
                isSubmitting = false
            ),
            onAction = { },
        )
    }
}
