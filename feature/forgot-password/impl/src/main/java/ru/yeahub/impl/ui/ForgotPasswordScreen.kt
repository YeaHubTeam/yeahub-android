@file:OptIn(ExperimentalMaterial3Api::class)

package ru.yeahub.impl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.YeahubButtonColors
//import ru.yeahub.core_ui.component.textInput.DefaultTextField
//import ru.yeahub.core_ui.component.textInput.TextInputColorsDefaults
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.impl.R
import ru.yeahub.impl.presentation.intents.ForgotPasswordEvent
import ru.yeahub.impl.presentation.state.ForgotPasswordScreenState

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    state: ForgotPasswordScreenState,
    onEvent: (ForgotPasswordEvent) -> Unit
) {
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

        val email = when (state) {
            is ForgotPasswordScreenState.Content -> state.email
            else -> ""
        }
        val isLoading = when (state) {
            is ForgotPasswordScreenState.Content -> state.isLoading
            else -> false
        }
        val emailError = when (state) {
            is ForgotPasswordScreenState.Content -> state.emailError
            else -> null
        }
        val isEmailValid = when (state) {
            is ForgotPasswordScreenState.Content -> state.isEmailValid
            else -> false
        }

//        DefaultTextField(
//            value = email,
//            onValueChange = { onEvent(ForgotPasswordEvent.EmailChanged(it)) },
//            label = stringResource(R.string.enter_email),
//            isEnabled = !isLoading,
//            isError = emailError != null,
//            showLeadingIcon = false,
//            onExpandedChange = { },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(52.dp),
////            colors = TextInputColorsDefaults.defaultColors()
//        )

        if (emailError != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = emailError,
                style = Theme.typography.body3,
                color = Theme.colors.red500
            )
        }

        Spacer(Modifier.height(20.dp))

        PrimaryButton(
            onClick = { onEvent(ForgotPasswordEvent.SubmitClicked) },
            enabled = isEmailValid && !isLoading,
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

@Preview(showBackground = true, name = "Initial State")
@Composable
fun ForgotPasswordScreenPreview_Initial() {
    ForgotPasswordScreen(
        state = ForgotPasswordScreenState.Initial,
        onEvent = {}
    )
}

@Preview(showBackground = true, name = "Valid Email")
@Composable
fun ForgotPasswordScreenPreview_Valid() {
    ForgotPasswordScreen(
        state = ForgotPasswordScreenState.Content(
            email = "user@example.com",
            isLoading = false,
            emailError = null,
            isSent = false
        ),
        onEvent = {}
    )
}

@Preview(showBackground = true, name = "Invalid Email")
@Composable
fun ForgotPasswordScreenPreview_Invalid() {
    ForgotPasswordScreen(
        state = ForgotPasswordScreenState.Content(
            email = "invalid-email",
            isLoading = false,
            emailError = "Введите корректный email",
            isSent = false
        ),
        onEvent = {}
    )
}

@Preview(showBackground = true, name = "Loading")
@Composable
fun ForgotPasswordScreenPreview_Loading() {
    ForgotPasswordScreen(
        state = ForgotPasswordScreenState.Content(
            email = "user@example.com",
            isLoading = true,
            emailError = null,
            isSent = false
        ),
        onEvent = {}
    )
}
