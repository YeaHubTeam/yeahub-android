package ru.yeahub.example_home.impl.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.yeahub.example_home.impl.presentation.intents.QuestionMainScreenCommand
import ru.yeahub.example_home.impl.presentation.intents.QuestionMainScreenEvent
import ru.yeahub.example_home.impl.presentation.mapper.QuestionMainScreenMapper
import ru.yeahub.example_home.impl.presentation.model.QuestionMainItemType
import ru.yeahub.example_home.impl.presentation.state.QuestionMainScreenState

class QuestionMainViewModel(
    private val domainMapper: QuestionMainScreenMapper,
) : ViewModel() {

    private val _command = MutableSharedFlow<QuestionMainScreenCommand>()
    val command: SharedFlow<QuestionMainScreenCommand> = _command

    private val initialStateFlow = flow {
        emit(QuestionMainScreenState.Loading)
        val uiModels = domainMapper.getInitialUiModels()
        emit(QuestionMainScreenState.Content(uiModels))
    }

    val state: StateFlow<QuestionMainScreenState> = initialStateFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = QuestionMainScreenState.Loading
        )

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

                        QuestionMainItemType.InterviewTrainer -> {
                            _command.emit(QuestionMainScreenCommand.NavigateToInterviewTrainer)
                        }
                    }
                }
            }
        }
    }
}
