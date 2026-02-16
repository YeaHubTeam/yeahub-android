package ru.yeahub.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yeahub.impl.domain.ForgotPasswordResult
import ru.yeahub.impl.domain.SendResetLinkUseCase

class ForgotPasswordViewModel(
    private val sendResetLink: SendResetLinkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state: StateFlow<ForgotPasswordState> = _state.asStateFlow()

    private val _effect = Channel<ForgotPasswordEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: ForgotPasswordIntent) {
        when (intent) {
            is ForgotPasswordIntent.EmailChanged -> onEmailChanged(intent.value)
            is ForgotPasswordIntent.SubmitClicked -> onSubmit()
            is ForgotPasswordIntent.BackClicked -> emitEffect(ForgotPasswordEffect.NavigateBack)
        }
    }

    private fun onEmailChanged(value: String) {
        _state.update {
            it.copy(
                email = value,
                isEmailError = false,
                emailErrorText = null,
                isSent = false
            )
        }
    }

    private fun onSubmit() {
        val email = state.value.email
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            when (val result = sendResetLink(email)) {
                is ForgotPasswordResult.Success -> {
                    _state.update { it.copy(isLoading = false, isSent = true) }
                    emitEffect(ForgotPasswordEffect.NavigateToCheckEmail)
                }

                is ForgotPasswordResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isEmailError = true,
                            emailErrorText = result.message
                        )
                    }
                    emitEffect(ForgotPasswordEffect.ShowSnackbar(result.message))
                }
            }
        }
    }

    private fun emitEffect(effect: ForgotPasswordEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}