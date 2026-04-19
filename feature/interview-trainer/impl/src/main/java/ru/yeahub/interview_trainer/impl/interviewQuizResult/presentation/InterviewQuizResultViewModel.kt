package ru.yeahub.interview_trainer.impl.interviewQuizResult.presentation

import InterviewQuizResultState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.interview_trainer.impl.R
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.QuizAnswerResult
import ru.yeahub.interview_trainer.impl.interviewQuiz.presentation.VoQuestionWithAnswer

private const val STOP_TIMEOUT_MILLIS = 5000L

class InterviewQuizResultViewModel(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val questions: List<VoQuestionWithAnswer> =
        savedStateHandle["quizAnswersKey"] ?: emptyList()

    val state: StateFlow<InterviewQuizResultState> =
        flow {
            emit(mapToState(questions))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = InterviewQuizResultState.Loading
        )

    private fun mapToState(
        questions: List<VoQuestionWithAnswer>
    ): InterviewQuizResultState.Loaded {
        val total = questions.size
        val correct = questions.count { it.userAnswer == QuizAnswerResult.KNOWN }

        return InterviewQuizResultState.Loaded(
            titleTopAppBar = TextOrResource.Resource(R.string.interview_quiz_result_result),
            overallPercentage = if (total == 0) 0f else correct.toFloat() / total,
            totalQuestions = total,
            newQuestions = 0,
            inProgress = 0,
            studied = correct,
            skills = persistentListOf(),
            questions = questions.map { question: VoQuestionWithAnswer ->
                InterviewQuizResultState.Loaded.VoQuestionResult(
                    questionText = question.title,
                    isCorrect = question.userAnswer == QuizAnswerResult.KNOWN
                )
            }.toPersistentList(),
        )
    }

    fun onEvent(event: InterviewQuizResultEvent) {
        // TODO
    }
}