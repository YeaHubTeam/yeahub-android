package ru.yeahub.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.yeahub.impl.presentation.ForgotPasswordEffect
import ru.yeahub.impl.presentation.ForgotPasswordViewModel

@Composable
fun ForgotPasswordRoute(
    viewModel: ForgotPasswordViewModel,
    onBack: () -> Unit,
    onCheckEmail: () -> Unit,
    showSnackbar: suspend (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ForgotPasswordEffect.NavigateBack -> onBack()
                is ForgotPasswordEffect.NavigateToCheckEmail -> onCheckEmail()
                is ForgotPasswordEffect.ShowSnackbar -> showSnackbar(effect.message)
            }
        }
    }

    ForgotPasswordScreen(
        state = state,
        onIntent = viewModel::onIntent
    )
}