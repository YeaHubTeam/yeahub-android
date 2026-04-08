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
import ru.yeahub.authentication.impl.login.presentation.model.LoginFieldErrorState
import ru.yeahub.authentication.impl.login.presentation.model.LoginRawState
import ru.yeahub.authentication.impl.login.presentation.model.LoginState
import ru.yeahub.core_utils.common.TextOrResource

private const val UI_STATE_STOP_TIMEOUT = 5000L

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

    private val rawState = MutableStateFlow<LoginRawState>(
        value = mapper.getInitialRawState(),
    )

    private val _commands = MutableSharedFlow<LoginCommand>()
    val commands = _commands.asSharedFlow()

    val state: StateFlow<LoginState> = rawState
        .map { currentRawState ->
            mapper.mapToScreenState(
                rawState = currentRawState,
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

            LoginAction.OnTogglePasswordVisible -> onTogglePasswordVisible()
            LoginAction.OnForgotPasswordClick -> onForgotPasswordClick()
            LoginAction.OnLoginClick -> onLoginClick()
            LoginAction.OnSignUpClick -> onSignUpClick()
        }
    }

    private fun onEmailChanged(value: String) {
        val email = value.trim()

        rawState.update { currentState ->
            when (currentState) {
                is LoginRawState.Initial -> LoginRawState.Editing(
                    email = email,
                    password = currentState.password,
                    isPasswordVisible = currentState.isPasswordVisible,
                )

                is LoginRawState.Editing -> currentState.copy(
                    email = email,
                )

                is LoginRawState.Validation -> currentState.copy(
                    email = email,
                )

                is LoginRawState.Loading -> currentState.copy(
                    email = email,
                )

                is LoginRawState.ServerError -> {
                    val updatedState = currentState.copy(
                        email = email,
                        emailError = null,
                    )

                    if (updatedState.emailError == null && updatedState.passwordError == null) {
                        LoginRawState.Validation(
                            email = updatedState.email,
                            password = updatedState.password,
                            isPasswordVisible = updatedState.isPasswordVisible,
                        )
                    } else {
                        updatedState
                    }
                }
            }
        }
    }

    private fun onPasswordChanged(value: String) {
        rawState.update { currentState ->
            when (currentState) {
                is LoginRawState.Initial -> LoginRawState.Editing(
                    email = currentState.email,
                    password = value,
                    isPasswordVisible = currentState.isPasswordVisible,
                )

                is LoginRawState.Editing -> currentState.copy(
                    password = value,
                )

                is LoginRawState.Validation -> currentState.copy(
                    password = value,
                )

                is LoginRawState.Loading -> currentState.copy(
                    password = value,
                )

                is LoginRawState.ServerError -> {
                    val updatedState = currentState.copy(
                        password = value,
                        passwordError = null,
                    )

                    if (updatedState.emailError == null && updatedState.passwordError == null) {
                        LoginRawState.Validation(
                            email = updatedState.email,
                            password = updatedState.password,
                            isPasswordVisible = updatedState.isPasswordVisible,
                        )
                    } else {
                        updatedState
                    }
                }
            }
        }
    }

    private fun onTogglePasswordVisible() {
        rawState.update { currentState ->
            when (currentState) {
                is LoginRawState.Initial -> currentState.copy(
                    isPasswordVisible = currentState.isPasswordVisible.not(),
                )

                is LoginRawState.Editing -> currentState.copy(
                    isPasswordVisible = currentState.isPasswordVisible.not(),
                )

                is LoginRawState.Validation -> currentState.copy(
                    isPasswordVisible = currentState.isPasswordVisible.not(),
                )

                is LoginRawState.Loading -> currentState.copy(
                    isPasswordVisible = currentState.isPasswordVisible.not(),
                )

                is LoginRawState.ServerError -> currentState.copy(
                    isPasswordVisible = currentState.isPasswordVisible.not(),
                )
            }
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
        if (rawState.value is LoginRawState.Loading) {
            return
        }

        val validationState = rawState.value.toValidationState()

        rawState.value = validationState

        val currentUiState = mapper.mapToScreenState(
            rawState = validationState,
        )

        if (currentUiState.formState.isSubmitEnabled.not()) {
            return
        }

        rawState.value = validationState.toLoadingState()

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

                rawState.value = currentRawState.toValidationState()

                sendCommand(
                    command = LoginCommand.NavigateToMain,
                )
            } catch (exception: LoginException) {
                rawState.value = rawState.value.toValidationState()

                handleLoginException(
                    exception = exception,
                )
            }
        }
    }

    private fun handleLoginException(exception: LoginException) {
        when (exception.error) {
            LoginError.InvalidCredentials -> {
                rawState.update { currentState ->
                    LoginRawState.ServerError(
                        email = currentState.email,
                        password = currentState.password,
                        isPasswordVisible = currentState.isPasswordVisible,
                        emailError = LoginFieldErrorState(
                            message = TextOrResource.Resource(
                                R.string.login_invalid_credentials,
                            ),
                        ),
                        passwordError = null,
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

    private fun LoginRawState.toValidationState(): LoginRawState.Validation {
        return when (this) {
            is LoginRawState.Initial -> LoginRawState.Validation(
                email = email,
                password = password,
                isPasswordVisible = isPasswordVisible,
            )

            is LoginRawState.Editing -> LoginRawState.Validation(
                email = email,
                password = password,
                isPasswordVisible = isPasswordVisible,
            )

            is LoginRawState.Validation -> this

            is LoginRawState.Loading -> LoginRawState.Validation(
                email = email,
                password = password,
                isPasswordVisible = isPasswordVisible,
            )

            is LoginRawState.ServerError -> LoginRawState.Validation(
                email = email,
                password = password,
                isPasswordVisible = isPasswordVisible,
            )
        }
    }

    private fun LoginRawState.Validation.toLoadingState(): LoginRawState.Loading {
        return LoginRawState.Loading(
            email = email,
            password = password,
            isPasswordVisible = isPasswordVisible,
        )
    }
}