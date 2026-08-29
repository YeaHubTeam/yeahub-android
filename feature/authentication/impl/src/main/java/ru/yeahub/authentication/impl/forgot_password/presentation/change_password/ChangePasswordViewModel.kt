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
import ru.yeahub.authentication.impl.forgot_password.presentation.change_password.model.ChangePasswordAction
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

    private val userInputState = MutableStateFlow(mapper.getInitialUserInput())

    internal val state = userInputState
        .map { userInput ->
            mapper.mapToScreenState(
                userInput = userInput,
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

    internal fun onAction(action: ChangePasswordAction) {
        when (action) {
            is ChangePasswordAction.OnPasswordChanged -> onPasswordChanged(action.value)
            is ChangePasswordAction.OnRepeatedPasswordChanged -> onRepeatedPasswordChanged(action.value)
            is ChangePasswordAction.OnPasswordFocusLost -> onPasswordFocusLost()
            is ChangePasswordAction.OnRepeatedPasswordFocusLost -> onRepeatedPasswordFocusLost()
            is ChangePasswordAction.OnTogglePasswordVisible -> onTogglePasswordVisible()
            is ChangePasswordAction.OnToggleRepeatedPasswordVisible -> onToggleRepeatedPasswordVisible()
            is ChangePasswordAction.OnSaveClick -> onSaveClick()
        }
    }

    private fun onPasswordChanged(value: String) {
        userInputState.update {
            it.copy(
                password = value,
                passwordServerError = null
            )
        }
    }

    private fun onRepeatedPasswordChanged(value: String) {
        userInputState.update {
            it.copy(
                repeatedPassword = value,
                passwordServerError = null
            )
        }
    }

    private fun onPasswordFocusLost() {
        userInputState.update {
            it.copy(
                isPasswordTouched = true
            )
        }
    }

    private fun onRepeatedPasswordFocusLost() {
        userInputState.update {
            it.copy(
                isRepeatedPasswordTouched = true
            )
        }
    }

    private fun onTogglePasswordVisible() {
        userInputState.update {
            it.copy(
                isPasswordVisible = !it.isPasswordVisible
            )
        }
    }

    private fun onToggleRepeatedPasswordVisible() {
        userInputState.update {
            it.copy(
                isRepeatedPasswordVisible = !it.isRepeatedPasswordVisible
            )
        }
    }

    private fun onSaveClick() {
        val validationInput = userInputState.value.copy(
            isPasswordTouched = true,
            isRepeatedPasswordTouched = true,
            isValidationRequested = true
        )

        userInputState.value = validationInput

        val currentUiState = mapper.mapToScreenState(
            userInput = validationInput,
            tokenError = tokenError,
        )
        if (!currentUiState.isSubmitEnabled || validationInput.isSubmitting) {
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
        userInputState.update {
            it.copy(
                isSubmitting = true
            )
        }
        viewModelScope.launch {
            when (
                val result = changePasswordUseCase(
                token = resetToken,
                password = validationInput.password
            )
            ) {
                is ForgotPasswordResult.Success -> {
                    userInputState.update {
                        it.copy(
                            isSubmitting = false
                        )
                    }
                    sendCommand(ChangePasswordCommand.NavigateToProfile)
                }

                is ForgotPasswordResult.Error -> {
                    userInputState.update {
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