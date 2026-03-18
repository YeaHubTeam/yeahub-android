package ru.yeahub.interview_trainer.impl.interviewQuizResult

sealed interface InterviewQuizResultEvent {

    data object Retry : InterviewQuizResultEvent
    data object ShareResults : InterviewQuizResultEvent
    data object ViewDetailedStats : InterviewQuizResultEvent
    data object OnBackClick : InterviewQuizResultEvent
}