package com.example.impl.presentation.viewmodel

import com.example.impl.domain.usecase.GetQuestionByIdUseCase
import com.example.impl.presentation.intents.DetailQuestionCommand
import com.example.impl.presentation.intents.DetailQuestionEvent
import com.example.impl.presentation.mapper.DetailQuestionScreenMapper
import com.example.impl.presentation.state.DetailQuestionState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import ru.yeahub.core_utils.BaseViewModel

class DetailQuestionViewModel(
    private val detailQuestionScreenMapper: DetailQuestionScreenMapper,
    private val getQuestionByIdUseCase: GetQuestionByIdUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<DetailQuestionState>(DetailQuestionState.Initial)
    val uiState: StateFlow<DetailQuestionState> get() = _uiState

    private val _commands = MutableSharedFlow<DetailQuestionCommand>()
    internal val commands: SharedFlow<DetailQuestionCommand> get() = _commands

    internal fun handleEvents(event: DetailQuestionEvent) {
        when (event) {
            is DetailQuestionEvent.Todo -> Unit
            is DetailQuestionEvent.LoadQuestion -> getQuestionById(event.id)

    @Suppress("TooGenericExceptionCaught")
    private fun getQuestionById(id: Long) {
        viewModelScopeSafe.launch {
            _uiState.value = DetailQuestionState.LoadingState
            try {
                val question = getQuestionByIdUseCase(id)
                _uiState.value = detailQuestionScreenMapper.getScreenState(question)
            } catch (e: Exception) {
                _uiState.value = DetailQuestionState.ErrorState(e.message)
            }
        }
    }

        }
    }
}