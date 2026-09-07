package ru.yeahub.authentication.impl.forgot_password.presentation.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.forgot_password.domain.ChangePasswordUseCase
import ru.yeahub.authentication.impl.forgot_password.domain.ForgotPasswordResult
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.mapper.ChangePasswordStateMapper
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model.ChangePasswordEvent
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model.ChangePasswordCommand
import ru.yeahub.core_utils.common.TextOrResource

private const val UI_STATE_STOP_TIMEOUT = 5_000L

class ChangePasswordViewModel(
    private val resetToken: String,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val mapper: ChangePasswordStateMapper,
) : ViewModel() {

    private val tokenError: TextOrResource? =
        if (resetToken.isBlank()) {
            TextOrResource.Resource(R.string.reset_link_invalid)
        } else {
            null
        }

    private val rawState = MutableStateFlow(mapper.getInitialRawState())

    internal val state = rawState
        .map { rawState ->
            mapper.mapToScreenState(
                rawState = rawState,
                tokenError = tokenError,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(UI_STATE_STOP_TIMEOUT),
            initialValue = mapper.getInitialState(tokenError = tokenError)
        )
    private val _commands = MutableSharedFlow<ChangePasswordCommand>()
    internal val commands = _commands.asSharedFlow()

    internal fun onEvent(event: ChangePasswordEvent) {
        when (event) {
            is ChangePasswordEvent.OnPasswordChanged -> onPasswordChanged(event.value)
            is ChangePasswordEvent.OnRepeatedPasswordChanged -> onRepeatedPasswordChanged(event.value)
            is ChangePasswordEvent.OnPasswordFocusLost -> onPasswordFocusLost()
            is ChangePasswordEvent.OnRepeatedPasswordFocusLost -> onRepeatedPasswordFocusLost()
            is ChangePasswordEvent.OnTogglePasswordVisible -> onTogglePasswordVisible()
            is ChangePasswordEvent.OnToggleRepeatedPasswordVisible -> onToggleRepeatedPasswordVisible()
            is ChangePasswordEvent.OnSaveClicked -> onSaveClick()
        }
    }

    private fun onPasswordChanged(value: String) {
        rawState.update {
            it.copy(
                password = value,
                passwordServerError = null
            )
        }
    }

    private fun onRepeatedPasswordChanged(value: String) {
        rawState.update {
            it.copy(
                repeatedPassword = value,
                passwordServerError = null
            )
        }
    }

    private fun onPasswordFocusLost() {
        rawState.update {
            it.copy(
                isPasswordTouched = true
            )
        }
    }

    private fun onRepeatedPasswordFocusLost() {
        rawState.update {
            it.copy(
                isRepeatedPasswordTouched = true
            )
        }
    }

    private fun onTogglePasswordVisible() {
        rawState.update {
            it.copy(
                isPasswordVisible = !it.isPasswordVisible
            )
        }
    }

    private fun onToggleRepeatedPasswordVisible() {
        rawState.update {
            it.copy(
                isRepeatedPasswordVisible = !it.isRepeatedPasswordVisible
            )
        }
    }

    private fun onSaveClick() {
        val validationRawState = rawState.value.copy(
            isPasswordTouched = true,
            isRepeatedPasswordTouched = true,
            isValidationRequested = true
        )

        rawState.value = validationRawState

        val currentUiState = mapper.mapToScreenState(
            rawState = validationRawState,
            tokenError = tokenError,
        )
        if (!currentUiState.isSubmitEnabled || validationRawState.isSubmitting) {
            return
        }
        if (resetToken.isBlank()) {
            sendCommand(
                ChangePasswordCommand.ShowSnackbar(
                    TextOrResource.Resource(R.string.reset_link_invalid)
                )
            )
            return
        }
        rawState.update {
            it.copy(
                isSubmitting = true
            )
        }
        viewModelScope.launch {
            when (
                val result = changePasswordUseCase(
                token = resetToken,
                password = validationRawState.password
            )
            ) {
                is ForgotPasswordResult.Success -> {
                    rawState.update {
                        it.copy(
                            isSubmitting = false
                        )
                    }
                    sendCommand(ChangePasswordCommand.NavigateToProfile)
                }

                is ForgotPasswordResult.Error -> {
                    rawState.update {
                        it.copy(
                            isSubmitting = false,
                            passwordServerError = TextOrResource.Text(result.message)
                        )
                    }
                    sendCommand(
                        ChangePasswordCommand.ShowSnackbar(
                            TextOrResource.Text(result.message)
                        )
                    )
                }
            }
        }
    }

    private fun sendCommand(command: ChangePasswordCommand) {
        viewModelScope.launch {
            _commands.emit(command)
        }
    }
}