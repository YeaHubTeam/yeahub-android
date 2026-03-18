package ru.yeahub.authentication.impl.registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationError
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationException
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationModel
import ru.yeahub.authentication.impl.registration.domain.usecase.RegistrationUseCase

private const val UI_STATE_STOP_TIMEOUT = 5000L

class RegistrationViewModel(
    private val registrationUseCase: RegistrationUseCase,
    private val mapper: RegistrationUiStateMapper
) : ViewModel() {

    private val formData = MutableStateFlow(mapper.getInitialFormState())
    private val isLoading = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)

    val state: StateFlow<RegistrationUiState> =
        combine(formData, isLoading, error) { form, loading, err ->
            mapper.getScreenState(form, loading, err)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(UI_STATE_STOP_TIMEOUT),
                initialValue = mapper.getInitialState()
            )

    fun onAction(action: RegistrationAction) {
        when (action) {
            is RegistrationAction.ConfirmPasswordChanged -> {
                updateForm { it.copy(confirmPassword = action.value) }
            }

            is RegistrationAction.EmailChanged -> {
                updateForm { it.copy(email = action.value) }
            }

            is RegistrationAction.MailingAcceptedChanged -> {
                updateForm { it.copy(isMailingAccepted = action.value) }
            }

            is RegistrationAction.NicknameChanged -> {
                updateForm { it.copy(nickname = action.value) }
            }

            is RegistrationAction.OfferAcceptedChanged -> {
                updateForm { it.copy(isOfferAccepted = action.value) }
            }

            is RegistrationAction.PasswordChanged -> {
                updateForm { it.copy(password = action.value) }
            }

            is RegistrationAction.PdAcceptedChanged -> {
                updateForm { it.copy(isPdAccepted = action.value) }
            }

            RegistrationAction.SubmitClicked -> {
                submitRegistration()
            }

            RegistrationAction.ToggleConfirmPasswordVisible -> {
                updateForm { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }

            RegistrationAction.TogglePasswordVisible -> {
                updateForm { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
        }
    }

    private fun updateForm(transform: (RegistrationFormState) -> RegistrationFormState) {
        formData.update { transform(it) }
        if (error.value != null) {
            error.value = null
        }
    }

    private fun submitRegistration() {
        isLoading.value = true
        error.value = null
        viewModelScope.launch {
            try {
                val currentForm = formData.value
                val userModel =
                    RegistrationModel(
                        nickname = currentForm.nickname,
                        email = currentForm.email,
                        password = currentForm.password,
                        isMailingAccepted = currentForm.isMailingAccepted
                    )
                registrationUseCase.invoke(userModel)
                isLoading.value = false
            } catch (e: RegistrationException) {
                val errorMessage =
                    when (e.error) {
                        RegistrationError.EmailAlreadyExists -> "Такой Email уже существует"
                        RegistrationError.NickNameTaken -> "Никнейм занят"
                        RegistrationError.InvalidCredentials -> "Неверные данные"
                        RegistrationError.Network -> "Ошибка сети. Проверьте подключение"
                        RegistrationError.Server, RegistrationError.Unknown ->
                            "Произошла ошибка на сервере"
                    }
                isLoading.value = false
                error.value = errorMessage
            }
        }
    }
}
