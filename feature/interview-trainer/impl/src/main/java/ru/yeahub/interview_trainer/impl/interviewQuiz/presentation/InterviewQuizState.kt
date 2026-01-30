package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

import androidx.compose.runtime.Immutable

sealed interface InterviewQuizState {

    /** Изначальное состояние */
    data object Loading : InterviewQuizState

    @Immutable
    data class Loaded(
        val questions: List<VoQuestion>,
        val questionsCount: Int,
        val currentQuestionIndex: Int,
        val isAnswerVisible: Boolean = false,
        val answers: Map<Long, QuizAnswer> = emptyMap(),
    ) : InterviewQuizState{

        val canGoNext: Boolean
            get() {
                return if (answers.containsKey(questions[currentQuestionIndex].id)
                    && currentQuestionIndex != questions.lastIndex) {
                    true
                } else {
                    false
                }
            }

        val canGoPrevious: Boolean
            get() = currentQuestionIndex > 0

        val currentAnswer: QuizAnswer
            get() = answers[questions[currentQuestionIndex].id]
                ?: QuizAnswer.NONE

        enum class QuizAnswer { KNOWN, UNKNOWN, NONE }

        @Immutable
        data class VoQuestion(
            val id: Long,
            val title: String,
            val shortAnswer: String
        )
    }

    data class Error(val throwable: Throwable) : InterviewQuizState
}