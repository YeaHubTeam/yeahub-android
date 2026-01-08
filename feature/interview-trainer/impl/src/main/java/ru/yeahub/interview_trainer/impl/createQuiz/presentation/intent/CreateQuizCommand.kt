package ru.yeahub.interview_trainer.impl.createQuiz.presentation.intent

sealed interface CreateQuizCommand {
    data class NavigateToInterviewQuizScreen(
        val specializationId: Long,
        val questionCount: Int
    ) : CreateQuizCommand

    data object NavigateBack : CreateQuizCommand
}