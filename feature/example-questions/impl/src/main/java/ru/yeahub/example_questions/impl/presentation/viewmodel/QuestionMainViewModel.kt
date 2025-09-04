package ru.yeahub.example_questions.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.yeahub.example_questions.impl.presentation.intents.QuestionMainScreenCommand
import ru.yeahub.example_questions.impl.presentation.intents.QuestionMainScreenEvent
import ru.yeahub.example_questions.impl.presentation.mapper.QuestionMainScreenMapper
import ru.yeahub.example_questions.impl.presentation.model.QuestionMainItemType
import ru.yeahub.example_questions.impl.presentation.state.QuestionMainScreenState

class QuestionMainViewModel(
    private val domainMapper: QuestionMainScreenMapper,
) : ViewModel() {

    private val _state = MutableStateFlow<QuestionMainScreenState>(QuestionMainScreenState.Loading)
    val state: StateFlow<QuestionMainScreenState> = _state

    private val _command = MutableSharedFlow<QuestionMainScreenCommand>()
    val command: SharedFlow<QuestionMainScreenCommand> = _command

    init {
        viewModelScope.launch {
            getInitialState()
        }
    }

    fun getInitialState() {
        val uiModels = domainMapper.getInitialUiModels()
        _state.value = QuestionMainScreenState.Content(uiModels)
    }

    fun onEvent(event: QuestionMainScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is QuestionMainScreenEvent.OnItemClick -> {
                    when (event.item.type) {
                        QuestionMainItemType.BaseQuestions -> _command.emit(
                            QuestionMainScreenCommand.NavigateToBaseQuestions
                        )
                        QuestionMainItemType.Collections ->
                            _command.emit(QuestionMainScreenCommand.NavigateToCollections)
                    }
                }
            }
        }
    }
}
