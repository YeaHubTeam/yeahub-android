package ru.yeahub.authentication.impl.forgot_password.presentation.email

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.yeahub.authentication.impl.forgot_password.presentation.email.model.ForgotPasswordEmailCommand
import ru.yeahub.core_utils.common.observe

@Composable
fun ForgotPasswordEmailRoute(
    viewModel: ForgotPasswordEmailViewModel,
    onBack: () -> Unit,
    showSnackbar: suspend (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val currentContext by rememberUpdatedState(LocalContext.current)
    val currentOnBack by rememberUpdatedState(onBack)
    val currentShowSnackbar by rememberUpdatedState(showSnackbar)

    viewModel.commands.observe(
        key = viewModel
    ) { command ->
        when (command) {
            is ForgotPasswordEmailCommand.NavigateBack -> currentOnBack()
            is ForgotPasswordEmailCommand.ShowSnackbar -> {
                currentShowSnackbar(command.message.getString(currentContext))
            }
        }
    }

    ForgotPasswordEmailScreen(
        state = state,
        onAction = viewModel::onAction
    )
}