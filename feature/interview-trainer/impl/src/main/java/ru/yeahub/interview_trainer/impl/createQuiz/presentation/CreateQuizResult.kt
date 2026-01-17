package ru.yeahub.interview_trainer.impl.createQuiz.presentation

sealed interface CreateQuizResult {
    data class NavigateToInterviewQuizScreen(
        val specializationId: Long,
        val questionCount: Int
    ) : CreateQuizResult

    data object NavigateBack : CreateQuizResult
}