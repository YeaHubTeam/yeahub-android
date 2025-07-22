package com.example.impl.presentation.viewmodel

import com.example.impl.presentation.intents.DetailQuestionCommand
import com.example.impl.presentation.intents.DetailQuestionEvent
import com.example.impl.presentation.mapper.DetailQuestionScreenMapper
import com.example.impl.presentation.view.DetailQuestionState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import ru.yeahub.core_utils.BaseViewModel

class DetailQuestionViewModel(detailQuestionScreenMapper: DetailQuestionScreenMapper) : BaseViewModel() {

    private val _uiState = MutableStateFlow(
        detailQuestionScreenMapper.getScreenState()
    )
    val uiState: StateFlow<DetailQuestionState> get() = _uiState

    private val _commands = MutableSharedFlow<DetailQuestionCommand>()
    internal val commands: SharedFlow<DetailQuestionCommand> get() = _commands

    internal fun handleEvents(event: DetailQuestionEvent) {
        when (event) {
            is DetailQuestionEvent.Todo -> Unit
        }
    }
}