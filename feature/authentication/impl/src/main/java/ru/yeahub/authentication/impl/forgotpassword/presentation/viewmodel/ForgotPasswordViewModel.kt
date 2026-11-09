package ru.yeahub.authentication.impl.forgotpassword.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.authentication.impl.forgotpassword.presentation.model.ForgotPasswordAction
import ru.yeahub.authentication.impl.forgotpassword.presentation.model.ForgotPasswordCommand
import ru.yeahub.authentication.impl.forgotpassword.presentation.model.ForgotPasswordState

class ForgotPasswordViewModel : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state = _state.asStateFlow()

    private val _commands = MutableSharedFlow<ForgotPasswordCommand>()
    val commands = _commands.asSharedFlow()

    fun onAction(action: ForgotPasswordAction) {
        when (action) {
            is ForgotPasswordAction.OnEmailChanged -> {
                _state.update {
                    it.copy(
                        email = action.email,
                        isSubmitEnabled = isEmailValid(action.email)
                    )
                }
            }

            ForgotPasswordAction.OnButtonClick -> {
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        isSuccessDialogVisible = true
                    )
                }
            }

            ForgotPasswordAction.OnBackToAuthClick -> {
                viewModelScope.launch {
                    _commands.emit(ForgotPasswordCommand.NavigateToSignIn)
                }
            }

            ForgotPasswordAction.OnReSendEmailClick -> {
                _state.update {
                    it.copy(isSuccessDialogVisible = false)
                }
            }
        }
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}