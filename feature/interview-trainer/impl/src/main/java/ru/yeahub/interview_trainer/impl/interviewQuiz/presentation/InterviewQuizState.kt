package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

import androidx.compose.runtime.Immutable

sealed interface InterviewQuizState {

    /** Изначальное состояние */
    data object Loading : InterviewQuizState

    @Immutable
    data class Loaded(
        val questions: List<VoQuestion>,
        val questionsCount: Int
    ) : InterviewQuizState{

        @Immutable
        data class VoQuestion(
            val id: Long,
            val title: String,
            val shortAnswer: String
        )
    }

    data class Error(val throwable: Throwable) : InterviewQuizState
}