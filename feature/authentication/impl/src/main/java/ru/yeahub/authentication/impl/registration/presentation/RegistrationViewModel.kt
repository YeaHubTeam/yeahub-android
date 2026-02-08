package ru.yeahub.authentication.impl.registration.presentation

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class RegistrationViewModel : ViewModel() {

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
            isSubmitEnabled = false
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

            RegistrationAction.SubmitClicked -> {}

            RegistrationAction.ToggleConfirmPasswordVisible -> {
                _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }

            RegistrationAction.TogglePasswordVisible -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
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