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
import ru.yeahub.authentication.impl.login.presentation.model.LoginErrorSource
import ru.yeahub.authentication.impl.login.presentation.model.LoginFieldErrorState
import ru.yeahub.authentication.impl.login.presentation.model.LoginState
import ru.yeahub.core_utils.common.TextOrResource

private const val UiStateStopTimeout = 5000L

/**
 * ViewModel логина:
 * - обрабатывает LoginAction
 * - обновляет состояние формы
 * - запускает авторизацию
 * - отправляет LoginCommand
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val mapper: LoginStateMapper,
) : ViewModel() {

    private val rawState = MutableStateFlow(
        value = mapper.getInitialRawState(),
    )

    val state: StateFlow<LoginState> = rawState
        .map { currentRawState ->
            mapper.mapToScreenState(
                rawState = currentRawState,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(UiStateStopTimeout),
            initialValue = mapper.getInitialState(),
        )

    private val _commands = MutableSharedFlow<LoginCommand>()
    val commands = _commands.asSharedFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChanged -> onEmailChanged(
                value = action.value,
            )

            is LoginAction.OnPasswordChanged -> onPasswordChanged(
                value = action.value,
            )

            LoginAction.OnTogglePasswordVisible -> onTogglePasswordVisible()
            LoginAction.OnForgotPasswordClick -> onForgotPasswordClick()
            LoginAction.OnLoginClick -> onLoginClick()
            LoginAction.OnSignUpClick -> onSignUpClick()
        }
    }

    private fun onEmailChanged(value: String) {
        rawState.update { currentState ->
            currentState.copy(
                email = value.trim(),
                emailServerError = null,
            )
        }
    }

    private fun onPasswordChanged(value: String) {
        rawState.update { currentState ->
            currentState.copy(
                password = value,
                passwordServerError = null,
            )
        }
    }

    private fun onTogglePasswordVisible() {
        rawState.update { currentState ->
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
        rawState.update { currentState ->
            currentState.copy(
                isValidationEnabled = true,
            )
        }

        val currentUiState = mapper.mapToScreenState(
            rawState = rawState.value,
        )

        if (currentUiState.formState.isSubmitEnabled.not()) {
            return
        }

        if (rawState.value.isLoading) {
            return
        }

        rawState.update { currentState ->
            currentState.copy(
                isLoading = true,
                emailServerError = null,
                passwordServerError = null,
            )
        }

        viewModelScope.launch {
            try {
                val currentRawState = rawState.value
                val loginModel = LoginModel(
                    email = currentRawState.email,
                    password = currentRawState.password,
                )

                loginUseCase.invoke(
                    loginModel = loginModel,
                )

                rawState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                    )
                }

                sendCommand(
                    command = LoginCommand.NavigateToMain,
                )
            } catch (exception: LoginException) {
                rawState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                    )
                }

                handleLoginException(
                    exception = exception,
                )
            } catch (exception: Exception) {
                rawState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                    )
                }

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

    private fun handleLoginException(exception: LoginException) {
        when (exception.error) {
            LoginError.InvalidCredentials -> {
                rawState.update { currentState ->
                    currentState.copy(
                        emailServerError = LoginFieldErrorState(
                            message = TextOrResource.Resource(
                                R.string.login_invalid_credentials,
                            ),
                            source = LoginErrorSource.SERVER,
                        ),
                    )
                }
            }

            LoginError.UserNotFound -> {
                sendCommand(
                    command = LoginCommand.ShowSnackbar(
                        message = TextOrResource.Resource(
                            R.string.login_user_not_found,
                        ),
                    ),
                )
            }

            LoginError.Network -> {
                sendCommand(
                    command = LoginCommand.ShowSnackbar(
                        message = TextOrResource.Resource(
                            R.string.login_network_error,
                        ),
                    ),
                )
            }

            LoginError.Server -> {
                sendCommand(
                    command = LoginCommand.ShowSnackbar(
                        message = TextOrResource.Resource(
                            R.string.login_server_error,
                        ),
                    ),
                )
            }

            LoginError.Unknown -> {
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