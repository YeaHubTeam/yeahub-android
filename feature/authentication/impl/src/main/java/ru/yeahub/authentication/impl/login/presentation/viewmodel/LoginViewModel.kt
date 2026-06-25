package ru.yeahub.authentication.impl.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.authentication.impl.R
import ru.yeahub.authentication.impl.login.domain.entity.LoginError
import ru.yeahub.authentication.impl.login.domain.entity.LoginException
import ru.yeahub.authentication.impl.login.domain.entity.LoginModel
import ru.yeahub.authentication.impl.login.domain.usecase.LoginUseCase
import ru.yeahub.authentication.impl.login.presentation.mapper.LoginStateMapper
import ru.yeahub.authentication.impl.login.presentation.model.LoginAction
import ru.yeahub.authentication.impl.login.presentation.model.LoginCommand
import ru.yeahub.authentication.impl.login.presentation.model.LoginState
import ru.yeahub.core_utils.common.TextOrResource

private const val UI_STATE_STOP_TIMEOUT = 5000L

/**
 * ViewModel логина:
 * - хранит пользовательский ввод
 * - формирует экранный state через LoginStateMapper
 * - запускает авторизацию
 * - отправляет LoginCommand
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val mapper: LoginStateMapper,
) : ViewModel() {
    private val userInputState = MutableStateFlow(
        value = mapper.getInitialUserInput(),
    )
    private val _commands = MutableSharedFlow<LoginCommand>()
    val commands = _commands.asSharedFlow()

    val state: StateFlow<LoginState> = userInputState
        .map { userInput ->
            mapper.mapToScreenState(
                userInput = userInput,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(UI_STATE_STOP_TIMEOUT),
            initialValue = mapper.getInitialState(),
        )

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChanged -> onEmailChanged(
                value = action.value,
            )

            is LoginAction.OnPasswordChanged -> onPasswordChanged(
                value = action.value,
            )

            is LoginAction.OnEmailFocusLost -> onEmailFocusLost()
            is LoginAction.OnPasswordFocusLost -> onPasswordFocusLost()
            is LoginAction.OnTogglePasswordVisible -> onTogglePasswordVisible()
            is LoginAction.OnForgotPasswordClick -> onForgotPasswordClick()
            is LoginAction.OnLoginClick -> onLoginClick()
            is LoginAction.OnSignUpClick -> onSignUpClick()
        }
    }

    private fun onEmailChanged(value: String) {
        val email = value.trim()

        userInputState.update { currentState ->
            currentState.copy(
                email = email,
                emailServerError = null,
            )
        }
    }

    private fun onPasswordChanged(value: String) {
        userInputState.update { currentState ->
            currentState.copy(
                password = value,
                passwordServerError = null,
            )
        }
    }

    private fun onEmailFocusLost() {
        userInputState.update { currentState ->
            currentState.copy(
                isEmailTouched = true,
            )
        }
    }

    private fun onPasswordFocusLost() {
        userInputState.update { currentState ->
            currentState.copy(
                isPasswordTouched = true,
            )
        }
    }

    private fun onTogglePasswordVisible() {
        userInputState.update { currentState ->
            currentState.copy(
                isPasswordVisible = currentState.isPasswordVisible.not(),
            )
        }
    }

    private fun onForgotPasswordClick() {
        sendCommand(
            command = LoginCommand.NavigateToForgotPassword,
        )
    }

    private fun onSignUpClick() {
        sendCommand(
            command = LoginCommand.NavigateToSignUp,
        )
    }

    private fun onLoginClick() {
        val currentUserInput = userInputState.value
        if (currentUserInput.isSubmitting) {
            return
        }
        val validationUserInput = currentUserInput.copy(
            isEmailTouched = true,
            isPasswordTouched = true,
            isValidationRequested = true,
        )

        userInputState.value = validationUserInput

        val currentUiState = mapper.mapToScreenState(
            userInput = validationUserInput,
        )
        if (currentUiState.isSubmitEnabled.not()) {
            return
        }
        userInputState.value = validationUserInput.copy(
            isSubmitting = true,
        )
        viewModelScope.launch {
            try {
                val submittingUserInput = userInputState.value

                val loginModel = LoginModel(
                    email = submittingUserInput.email,
                    password = submittingUserInput.password,
                )
                loginUseCase.invoke(
                    loginModel = loginModel,
                )
                userInputState.update { currentState ->
                    currentState.copy(
                        isSubmitting = false,
                    )
                }
                sendCommand(
                    command = LoginCommand.NavigateToMain,
                )
            } catch (exception: LoginException) {
                userInputState.update { currentState ->
                    currentState.copy(
                        isSubmitting = false,
                    )
                }

                handleLoginException(
                    exception = exception,
                )
            }
        }
    }

    private fun handleLoginException(exception: LoginException) {
        when (exception.error) {
            is LoginError.InvalidCredentials -> {
                userInputState.update { currentState ->
                    currentState.copy(
                        isEmailTouched = true,
                        isPasswordTouched = true,
                        isValidationRequested = true,
                        emailServerError = TextOrResource.Resource(
                            R.string.login_invalid_credentials,
                        ),
                        passwordServerError = null,
                    )
                }
            }

            is LoginError.UserNotFound -> {
                sendCommand(
                    command = LoginCommand.ShowSnackbar(
                        message = TextOrResource.Resource(
                            R.string.login_user_not_found,
                        ),
                    ),
                )
            }

            is LoginError.Network -> {
                sendCommand(
                    command = LoginCommand.ShowSnackbar(
                        message = TextOrResource.Resource(
                            R.string.login_network_error,
                        ),
                    ),
                )
            }

            is LoginError.Server -> {
                sendCommand(
                    command = LoginCommand.ShowSnackbar(
                        message = TextOrResource.Resource(
                            R.string.login_server_error,
                        ),
                    ),
                )
            }

            is LoginError.TokenSaveFailed -> {
                sendCommand(
                    command = LoginCommand.ShowSnackbar(
                        message = TextOrResource.Resource(
                            R.string.login_token_save_error,
                        ),
                    ),
                )
            }

            else -> {
                sendCommand(
                    command = LoginCommand.ShowSnackbar(
                        message = TextOrResource.Resource(
                            R.string.login_unknown_error,
                        ),
                    ),
                )
            }
        }
    }

    private fun sendCommand(command: LoginCommand) {
        viewModelScope.launch {
            _commands.emit(command)
        }
    }
}