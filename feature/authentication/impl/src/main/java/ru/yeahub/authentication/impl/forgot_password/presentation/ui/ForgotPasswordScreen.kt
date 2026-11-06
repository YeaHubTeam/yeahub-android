package ru.yeahub.authentication.impl.forgot_password.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.authentication.impl.R
import ru.yeahub.core_ui.theme.YeaHubTheme

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.forgot_password)
        )
        Text(
            text = stringResource(R.string.enter_email_instruction)
        )
    }
}

@Preview(name = "Start state", showBackground = true)
@Composable
fun ForgotPasswordScreenStartPreview() {
    YeaHubTheme {
        Text(
            text = "Preview test",
            color = Color.Black
        )
    }
}

@Preview(name = "Invalid email", showBackground = true)
@Composable
fun ForgotPasswordScreenInvalidEmailPreview() {
    YeaHubTheme {
        ForgotPasswordScreen(
            modifier = Modifier
        )
    }
}

@Preview(name = "Correct email", showBackground = true)
@Composable
fun ForgotPasswordScreenCorrectEmailPreview() {
    YeaHubTheme {
        ForgotPasswordScreen(
            modifier = Modifier
        )
    }
}

@Preview(name = "Mail sent", showBackground = true)
@Composable
fun ForgotPasswordScreenDialogPreview() {
    YeaHubTheme {
        ForgotPasswordScreen(
            modifier = Modifier
        )
    }
}
