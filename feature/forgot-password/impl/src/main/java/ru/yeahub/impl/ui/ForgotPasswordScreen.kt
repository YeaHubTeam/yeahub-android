@file:OptIn(ExperimentalMaterial3Api::class)

package ru.yeahub.impl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.YeahubButtonColors
import ru.yeahub.core_ui.component.textInput.DefaultTextField
import ru.yeahub.core_ui.component.textInput.TextInputColorsDefaults
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.impl.R
import ru.yeahub.impl.presentation.ForgotPasswordIntent
import ru.yeahub.impl.presentation.ForgotPasswordState

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    state: ForgotPasswordState,
    onIntent: (ForgotPasswordIntent) -> Unit
) {
    val isEmailValid = state.email.contains("@") && state.email.contains(".")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp)
    ) {
        Text(
            text = stringResource(R.string.title_forgot_password),
            style = Theme.typography.head5
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.enter_email_instruction),
            style = Theme.typography.body2Accent,
            color = Theme.colors.black500
        )

        Spacer(Modifier.height(96.dp))

        Text(
            text = stringResource(R.string.email),
            style = Theme.typography.body3Accent
        )

        Spacer(Modifier.height(6.dp))
        
        DefaultTextField(
            value = state.email,
            onValueChange = { onIntent(ForgotPasswordIntent.EmailChanged(it)) },
            label = stringResource(R.string.enter_email),
            isEnabled = !state.isLoading,
            isError = state.isEmailError,
            showLeadingIcon = false,
            onExpandedChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = TextInputColorsDefaults.defaultColors()
        )

        Spacer(Modifier.height(20.dp))

        PrimaryButton(
            onClick = { onIntent(ForgotPasswordIntent.SubmitClicked) },
            enabled = isEmailValid && !state.isLoading,
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
            Text(
                text = stringResource(R.string.send_button),
                style = Theme.typography.body4
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        state = ForgotPasswordState(),
        onIntent = {}
    )
}
