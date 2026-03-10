package ru.yeahub.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.yeahub.impl.presentation.ForgotPasswordViewModel
import ru.yeahub.impl.presentation.intents.ForgotPasswordCommand

@Composable
fun ForgotPasswordRoute(
    viewModel: ForgotPasswordViewModel,
    onBack: () -> Unit,
    onCheckEmail: () -> Unit,
    showSnackbar: suspend (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.commands.collect { command ->
            when (command) {
                is ForgotPasswordCommand.NavigateBack -> onBack()
                is ForgotPasswordCommand.NavigateToCheckEmail -> onCheckEmail()
                is ForgotPasswordCommand.ShowSnackbar -> showSnackbar(command.message)
            }
        }
    }

    ForgotPasswordScreen(
        state = state,
        onEvent = viewModel::handleEvents
    )
}