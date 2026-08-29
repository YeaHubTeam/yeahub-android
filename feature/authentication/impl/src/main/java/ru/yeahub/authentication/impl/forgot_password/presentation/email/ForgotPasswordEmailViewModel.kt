package ru.yeahub.authentication.impl.forgot_password.presentation.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.authentication.impl.forgot_password.domain.ForgotPasswordResult
import ru.yeahub.authentication.impl.forgot_password.domain.SendResetLinkUseCase
import ru.yeahub.authentication.impl.forgot_password.presentation.email.mapper.ForgotPasswordEmailStateMapper
import ru.yeahub.authentication.impl.forgot_password.presentation.email.model.ForgotPasswordEmailAction
import ru.yeahub.authentication.impl.forgot_password.presentation.email.model.ForgotPasswordEmailCommand
import ru.yeahub.authentication.impl.forgot_password.presentation.email.model.ForgotPasswordEmailState
import ru.yeahub.core_utils.common.TextOrResource
import kotlin.time.Duration.Companion.milliseconds

private const val UI_STATE_STOP_TIMEOUT = 5000L
private const val RESEND_COOLDOWN_SECONDS = 60

class ForgotPasswordEmailViewModel(
    private val sendResetLinkUseCase: SendResetLinkUseCase,
    private val mapper: ForgotPasswordEmailStateMapper
) : ViewModel() {

    private val emailFormState = MutableStateFlow(
        mapper.getInitialUserInput()
    )
    private var cooldownJob: Job? = null

    private val _commands = MutableSharedFlow<ForgotPasswordEmailCommand>()
    internal val commands = _commands.asSharedFlow()

    internal val state: StateFlow<ForgotPasswordEmailState> = emailFormState
        .map(mapper::mapToScreenState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(UI_STATE_STOP_TIMEOUT),
            initialValue = mapper.getInitialState()
        )

    internal fun onAction(action: ForgotPasswordEmailAction) {
        when (action) {
            is ForgotPasswordEmailAction.OnBackClick -> sendCommand(ForgotPasswordEmailCommand.NavigateBack)
            is ForgotPasswordEmailAction.OnDismissSuccessDialog -> onDismissSuccessDialog()
            is ForgotPasswordEmailAction.OnEmailChanged -> onEmailChanged(action.value)
            is ForgotPasswordEmailAction.OnEmailFocusLost -> onEmailFocusLost()
            is ForgotPasswordEmailAction.OnResendClick -> onSubmitClick()
            is ForgotPasswordEmailAction.OnSubmitClick -> onSubmitClick()
        }
    }

    private fun onEmailChanged(value: String) {
        emailFormState.update { currentState ->
            currentState.copy(
                email = value.trim(),
                emailServerError = null
            )
        }
    }

    private fun onEmailFocusLost() {
        emailFormState.update { it.copy(isEmailTouched = true) }
    }

    private fun onDismissSuccessDialog() {
        emailFormState.update { it.copy(isSuccessDialogVisible = false) }
    }

    private fun onSubmitClick() {
        val currentInput = emailFormState.value

        if (currentInput.isSubmitting || currentInput.isCooldownActive) {
            return
        }

        val validationInput = currentInput.copy(
            isEmailTouched = true,
            isValidationRequested = true
        )
        emailFormState.value = validationInput

        val currentUiState = mapper.mapToScreenState(validationInput)
        if (!currentUiState.isSubmitEnabled) {
            return
        }

        emailFormState.update {
            it.copy(
                isSubmitting = true,
                emailServerError = null
            )
        }

        viewModelScope.launch {
            when (val result = sendResetLinkUseCase(validationInput.email)) {
                is ForgotPasswordResult.Success -> {
                    emailFormState.update {
                        it.copy(
                            isSubmitting = false,
                            isSuccessDialogVisible = true,
                            cooldownSecondsLeft = RESEND_COOLDOWN_SECONDS
                        )
                    }
                    startCooldown()
                }

                is ForgotPasswordResult.Error -> {
                    emailFormState.update {
                        it.copy(
                            isSubmitting = false,
                            emailServerError = TextOrResource.Text(result.message)
                        )
                    }
                    sendCommand(
                        ForgotPasswordEmailCommand.ShowSnackbar(
                            TextOrResource.Text(result.message)
                        )
                    )
                }
            }
        }
    }

    private fun startCooldown() {
        cooldownJob?.cancel()
        cooldownJob = viewModelScope.launch {
            while (emailFormState.value.cooldownSecondsLeft > 0) {
                delay(1000L.milliseconds)
                emailFormState.update { currentState ->
                    currentState.copy(
                    cooldownSecondsLeft = currentState.cooldownSecondsLeft - 1
                    )
                }
            }
        }
    }

    private fun sendCommand(command: ForgotPasswordEmailCommand) {
        viewModelScope.launch {
            _commands.emit(command)
        }
    }
}
