package ru.yeahub.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.impl.domain.ForgotPasswordResult
import ru.yeahub.impl.domain.SendResetLinkUseCase
import ru.yeahub.impl.presentation.intents.ForgotPasswordCommand
import ru.yeahub.impl.presentation.intents.ForgotPasswordEvent
import ru.yeahub.impl.presentation.mapper.ForgotPasswordScreenMapper
import ru.yeahub.impl.presentation.state.ForgotPasswordScreenState
import ru.yeahub.impl.presentation.state.ForgotPasswordState

@OptIn(ExperimentalCoroutinesApi::class)
class ForgotPasswordViewModel(
    private val forgotPasswordScreenMapper: ForgotPasswordScreenMapper,
    private val sendResetLinkUseCase: SendResetLinkUseCase
) : ViewModel() {

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }

    private val mutableState =
        MutableStateFlow<ForgotPasswordState>(
        ForgotPasswordState.Content(
            email = "",
            emailValidationError = null,
            isSuccessDialogVisible = false,
        )
    )

    val uiState: StateFlow<ForgotPasswordScreenState> = mutableState
        .mapLatest { state -> forgotPasswordScreenMapper.getScreenState(state) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = ForgotPasswordScreenState.Initial
        )

    private val mutableCommands =
        MutableSharedFlow<ForgotPasswordCommand>()
    val commands: SharedFlow<ForgotPasswordCommand> = mutableCommands

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged -> onEmailChanged(event.value)
            is ForgotPasswordEvent.SubmitClicked -> onSubmit()
            is ForgotPasswordEvent.BackClicked -> handleBackClick()
        }
    }

    private fun onEmailChanged(value: String) {
        val validationError = if (value.isNotBlank()) {
            forgotPasswordScreenMapper.validateEmail(value)
        } else {
            null
        }

        mutableState.update {
            ForgotPasswordState.Content(
                email = value,
                emailValidationError = validationError,
                isSuccessDialogVisible = false,
            )
        }
    }

    private fun onSubmit() {
        val currentState = mutableState.value
        val email = currentState.email.trim()

        if (!forgotPasswordScreenMapper.canSubmit(email)) {
            mutableState.update {
                ForgotPasswordState.Content(
                    email = email,
                    emailValidationError = forgotPasswordScreenMapper.validateEmail(email),
                    isSuccessDialogVisible = false,
                )
            }
            return
        }

        mutableState.update {
            ForgotPasswordState.Loading(
                email = email,
                emailValidationError = null,
            )
        }

        viewModelScope.launch {
            when (val result = sendResetLinkUseCase(email)) {
                is ForgotPasswordResult.Success -> {
                    mutableState.update {
                        ForgotPasswordState.Content(
                            email = email,
                            emailValidationError = null,
                            isSuccessDialogVisible = true,
                        )
                    }
                    emitCommand(ForgotPasswordCommand.NavigateToCheckEmail)
                }

                is ForgotPasswordResult.Error -> {
                    mutableState.update {
                        ForgotPasswordState.Error(
                            email = email,
                            error = result.message,
                            emailValidationError = null,
                        )
                    }
                    emitCommand(ForgotPasswordCommand.ShowSnackbar(result.message))
                }
            }
        }
    }

    private fun handleBackClick() {
        viewModelScope.launch {
            mutableCommands.emit(ForgotPasswordCommand.NavigateBack)
        }
    }

    private fun emitCommand(command: ForgotPasswordCommand) {
        viewModelScope.launch {
            mutableCommands.emit(command)
        }
    }

    private val ForgotPasswordState.email: String
        get() = when (this) {
            is ForgotPasswordState.Content -> email
            is ForgotPasswordState.Loading -> email
            is ForgotPasswordState.Error -> email
        }
}
