package ru.yeahub.authentication.impl.registration.presentation

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationError
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationException
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationModel
import ru.yeahub.authentication.impl.registration.domain.usecase.RegistrationUseCase

class RegistrationViewModel(
    private val registrationUseCase: RegistrationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(
        RegistrationUiState(
            nickname = "",
            email = "",
            password = "",
            confirmPassword = "",
            isPdAccepted = false,
            isOfferAccepted = false,
            isMailingAccepted = false,
            isPasswordVisible = false,
            isConfirmPasswordVisible = false,
            isSubmitEnabled = false,
            isLoading = false,
            error = null
        )
    )
    val state: StateFlow<RegistrationUiState> = _state

    fun onAction(action: RegistrationAction) {
        when (action) {
            is RegistrationAction.ConfirmPasswordChanged -> {
                _state.update { it.copy(confirmPassword = action.value).revalidate() }
            }

            is RegistrationAction.EmailChanged -> {
                _state.update { it.copy(email = action.value).revalidate() }
            }

            is RegistrationAction.MailingAcceptedChanged -> {
                _state.update { it.copy(isMailingAccepted = action.value).revalidate() }
            }

            is RegistrationAction.NicknameChanged -> {
                _state.update { it.copy(nickname = action.value).revalidate() }
            }

            is RegistrationAction.OfferAcceptedChanged -> {
                _state.update { it.copy(isOfferAccepted = action.value).revalidate() }
            }

            is RegistrationAction.PasswordChanged -> {
                _state.update { it.copy(password = action.value).revalidate() }
            }

            is RegistrationAction.PdAcceptedChanged -> {
                _state.update { it.copy(isPdAccepted = action.value).revalidate() }
            }

            RegistrationAction.SubmitClicked -> {
                submitRegistration()
            }

            RegistrationAction.ToggleConfirmPasswordVisible -> {
                _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }

            RegistrationAction.TogglePasswordVisible -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
        }
    }

    private fun submitRegistration() {
        val currentState = _state.value
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val userModel = RegistrationModel(
                    nickname = currentState.nickname,
                    email = currentState.email,
                    password = currentState.password,
                    isMailingAccepted = currentState.isMailingAccepted
                )
                registrationUseCase.invoke(userModel)
                _state.update { it.copy(isLoading = true) }
            } catch (e: RegistrationException) {
                val errorMassage = when (e.error) {
                    RegistrationError.EmailAlreadyExists -> "Такой Email уже существует"
                    RegistrationError.NickNameTaken -> "Никнейм занят"
                    RegistrationError.InvalidCredentials -> "Неверные данные"
                    RegistrationError.Network -> "Ошибка сети. Проверьте подключение"
                    RegistrationError.Server, RegistrationError.Unknown -> "Произошла ошибка на сервере"
                }
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = errorMassage
                    )
                }
            }
        }
    }

    private fun RegistrationUiState.revalidate(): RegistrationUiState {
        val nicknameOk = nickname.trim().isNotEmpty()
        val emailOk = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
        val passOk = isPasswordValid(password)
        val confirmOk = password == confirmPassword && confirmPassword.isNotEmpty()
        val requiredConsentsOk = isPdAccepted && isOfferAccepted

        return copy(
            isSubmitEnabled = nicknameOk && emailOk && passOk && confirmOk && requiredConsentsOk
        )
    }
}