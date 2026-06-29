package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

sealed interface InterviewQuizCommand {

    data class NavigateToInterviewQuizResultScreen(
        val questionsWithAnswersList: List<VoQuestionWithAnswer>
    ) : InterviewQuizCommand

    data object NavigateBack : InterviewQuizCommand
}