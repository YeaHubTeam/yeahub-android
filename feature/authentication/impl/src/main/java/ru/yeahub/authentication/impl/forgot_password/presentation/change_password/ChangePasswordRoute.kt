package ru.yeahub.authentication.impl.forgot_password.presentation.change_password

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model.ChangePasswordCommand
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.ui.ChangePasswordScreen
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
        onAction = viewModel::onAction,
    )
}