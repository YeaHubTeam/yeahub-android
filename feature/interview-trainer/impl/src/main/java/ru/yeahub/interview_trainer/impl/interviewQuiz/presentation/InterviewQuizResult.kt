package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

sealed interface InterviewQuizResult {

    data class NavigateToInterviewQuizResultScreen(
        val questionsWithAnswersList: List<VoQuestionWithAnswer>
    ) : InterviewQuizResult

    data object NavigateBack : InterviewQuizResult
}