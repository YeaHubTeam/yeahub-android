package ru.yeahub.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.impl.domain.ForgotPasswordResult
import ru.yeahub.impl.domain.SendResetLinkUseCase
import ru.yeahub.impl.presentation.intents.ForgotPasswordCommand
import ru.yeahub.impl.presentation.intents.ForgotPasswordEvent
import ru.yeahub.impl.presentation.mapper.ForgotPasswordScreenMapper
import ru.yeahub.impl.presentation.state.ForgotPasswordScreenState
import ru.yeahub.impl.presentation.state.ForgotPasswordState

class ForgotPasswordViewModel(
    private val forgotPasswordScreenMapper: ForgotPasswordScreenMapper,
    private val sendResetLinkUseCase: SendResetLinkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(
        ForgotPasswordState(
            email = "",
            isLoading = false,
            error = null,
            emailValidationError = "",
            isSuccessDialogVisible = false,
        )
    )

    private val _uiState =
        MutableStateFlow<ForgotPasswordScreenState>(ForgotPasswordScreenState.Initial)
    val uiState: StateFlow<ForgotPasswordScreenState> = _uiState.asStateFlow()

    private val _commands =
        MutableSharedFlow<ForgotPasswordCommand>()
    val commands: SharedFlow<ForgotPasswordCommand> = _commands

    init {
        viewModelScope.launch {
            _state.collect { state ->
                _uiState.value = forgotPasswordScreenMapper.getScreenState(state)
            }
        }
    }

    fun handleEvents(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged -> onEmailChanged(event.value)
            is ForgotPasswordEvent.SubmitClicked -> onSubmit()
            is ForgotPasswordEvent.BackClicked -> handleBackClick()
        }
    }

    private fun onEmailChanged(value: String) {
        _state.update { currentState ->
            val validationError = if (value.isNotBlank()) {
                forgotPasswordScreenMapper.validateEmail(value)
            } else {
                null
            }
            currentState.copy(
                email = value,
                emailValidationError = validationError,
                error = null
            )
        }
    }

    private fun onSubmit() {
        val currentState = _state.value
        val email = currentState.email.trim()

        if (!forgotPasswordScreenMapper.canSubmit(email)) {
            _state.update {
                it.copy(emailValidationError = forgotPasswordScreenMapper.validateEmail(email))
            }
            return
        }

        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            when (val result = sendResetLinkUseCase(email)) {
                is ForgotPasswordResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isSuccessDialogVisible = true
                        )
                    }
                    emitCommand(ForgotPasswordCommand.NavigateToCheckEmail)
                }

                is ForgotPasswordResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                    emitCommand(ForgotPasswordCommand.ShowSnackbar(result.message))
                }
            }
        }
    }

    private fun handleBackClick() {
        viewModelScope.launch {
            _commands.emit(ForgotPasswordCommand.NavigateBack)
        }
    }

    private fun emitCommand(command: ForgotPasswordCommand) {
        viewModelScope.launch {
            _commands.emit(command)
        }
    }
}