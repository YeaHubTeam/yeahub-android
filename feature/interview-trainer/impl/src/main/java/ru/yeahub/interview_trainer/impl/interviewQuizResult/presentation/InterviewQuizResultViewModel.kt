package ru.yeahub.interview_trainer.impl.interviewQuizResult.presentation

import InterviewQuizResultState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.VoQuestionWithAnswer

private const val STOP_TIMEOUT_MILLIS = 5000L

class InterviewQuizResultViewModel(
    savedStateHandle: SavedStateHandle,
    private val mapper: InterviewQuizResultScreenMapper
) : BaseViewModel() {

    private val questions: List<VoQuestionWithAnswer> =
        savedStateHandle["quizAnswersKey"] ?: emptyList()

    val state: StateFlow<InterviewQuizResultState> =
        flow {
            emit(mapper.getScreenState(questions))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = InterviewQuizResultState.Loading
        )

    fun onEvent(event: InterviewQuizResultEvent) {
        // TODO
    }
}