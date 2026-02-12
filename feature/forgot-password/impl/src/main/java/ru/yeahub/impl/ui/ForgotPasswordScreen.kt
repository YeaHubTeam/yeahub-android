@file:OptIn(ExperimentalMaterial3Api::class)

package ru.yeahub.impl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.impl.R
import ru.yeahub.impl.presentation.ForgotPasswordIntent
import ru.yeahub.impl.presentation.ForgotPasswordState

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    state: ForgotPasswordState,
    onIntent: (ForgotPasswordIntent) -> Unit
) {
    var email by remember { mutableStateOf("") }

    val isEmailValid = email.contains("@") && email.contains(".")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 32.dp)
    ) {
        Text(
            text = stringResource(R.string.title_forgot_password),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.enter_email_instruction),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(48.dp))

        Text(
            text = stringResource(R.string.email),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(10.dp))
        //DefaultTextField TextInput
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(text = stringResource(R.string.enter_email))},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledIndicatorColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )

        Spacer(Modifier.height(20.dp))

        // DisabledSecondaryButton
        // EnabledPrimaryButton
        PrimaryButton(
            onClick = { onIntent(ForgotPasswordIntent.SubmitClicked) },
            enabled = isEmailValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Text(stringResource(R.string.send_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenPreview() {
        ForgotPasswordScreen(
            state = ForgotPasswordState(),
            modifier = TODO(),
            onIntent = TODO(),
        )
}
