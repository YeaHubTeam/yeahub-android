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

    private val mutableState = MutableStateFlow(
        ForgotPasswordState(
            email = "",
            isLoading = false,
            error = null,
            emailValidationError = "",
            isSuccessDialogVisible = false,
        )
    )

    val uiState: StateFlow<ForgotPasswordScreenState> = mutableState
        .mapLatest { state -> forgotPasswordScreenMapper
            .getScreenState(state)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
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
        mutableState.update { currentState ->
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
        val currentState = mutableState.value
        val email = currentState.email.trim()

        if (!forgotPasswordScreenMapper.canSubmit(email)) {
            mutableState.update {
                it.copy(emailValidationError = forgotPasswordScreenMapper.validateEmail(email))
            }
            return
        }

        mutableState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            when (val result = sendResetLinkUseCase(email)) {
                is ForgotPasswordResult.Success -> {
                    mutableState.update {
                        it.copy(
                            isLoading = false,
                            isSuccessDialogVisible = true
                        )
                    }
                    emitCommand(ForgotPasswordCommand.NavigateToCheckEmail)
                }

                is ForgotPasswordResult.Error -> {
                    mutableState.update {
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
            mutableCommands.emit(ForgotPasswordCommand.NavigateBack)
        }
    }

    private fun emitCommand(command: ForgotPasswordCommand) {
        viewModelScope.launch {
            mutableCommands.emit(command)
        }
    }
}