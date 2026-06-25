package ru.yeahub.authentication.impl.registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationException
import ru.yeahub.authentication.impl.registration.domain.entity.RegistrationModel
import ru.yeahub.authentication.impl.registration.domain.usecase.RegistrationUseCase
import ru.yeahub.core_utils.common.TextOrResource

sealed interface RegistrationCommand {
    data object NavigateToSuccess : RegistrationCommand
    data class ShowError(val message: TextOrResource) : RegistrationCommand
}

class RegistrationViewModel(
    private val registrationUseCase: RegistrationUseCase,
    private val mapper: RegistrationUiStateMapper
) : ViewModel() {

    private val _state = MutableStateFlow(mapper.mapToInitialState())
    val state: StateFlow<RegistrationUiState> = _state.asStateFlow()

    private val _commands = MutableSharedFlow<RegistrationCommand>()
    val commands: SharedFlow<RegistrationCommand> = _commands.asSharedFlow()

    fun onAction(action: RegistrationAction) {
        when (action) {
            RegistrationAction.SubmitClicked -> submitRegistration()
            else -> {
                _state.value = mapper.mapToUpdatedState(_state.value, action)
            }
        }
    }

    private fun submitRegistration() {
        _state.value = mapper.mapToLoadingState(_state.value)
        viewModelScope.launch {
            try {
                val currentForm = _state.value.formState
                val userModel = RegistrationModel(
                    nickname = currentForm.nickname,
                    email = currentForm.email,
                    password = currentForm.password,
                    isMailingAccepted = currentForm.isMailingAccepted
                )
                registrationUseCase.invoke(userModel)
                _state.value = mapper.mapToInitialState()
                _commands.emit(RegistrationCommand.NavigateToSuccess)
            } catch (e: RegistrationException) {
                val errorResource = mapper.mapExceptionToResource(e)
                _state.value = mapper.mapToErrorState(_state.value, e)
                _commands.emit(RegistrationCommand.ShowError(errorResource))
            }
        }
    }
}
