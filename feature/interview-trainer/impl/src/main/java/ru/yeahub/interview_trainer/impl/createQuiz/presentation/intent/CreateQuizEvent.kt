package ru.yeahub.interview_trainer.impl.createQuiz.presentation.intent

sealed interface CreateQuizEvent {
    data class OnSpecializationClick(val specializationId: Long) : CreateQuizEvent

    data class OnPlusQuestionClick(val questionsCount: Int) : CreateQuizEvent

    data class OnMinusQuestionClick(val questionsCount: Int) : CreateQuizEvent

    data class OnStartInterviewQuizClick(
        val specializationId: Long,
        val questionCount: Int
    ) : CreateQuizEvent

    data object OnBackClick : CreateQuizEvent
}