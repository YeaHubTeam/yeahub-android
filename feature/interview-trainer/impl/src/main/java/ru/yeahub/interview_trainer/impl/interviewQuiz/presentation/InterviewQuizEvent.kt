package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

sealed interface InterviewQuizEvent {

    data object OnShowResultClick : InterviewQuizEvent

    data object OnKnownAnswerClick : InterviewQuizEvent

    data object OnUnknownAnswerClick : InterviewQuizEvent

    data object OnNextQuestionClick : InterviewQuizEvent

    data object OnPreviousQuestionClick : InterviewQuizEvent

    data object OnShowHideAnswerClick : InterviewQuizEvent

    data object OnBackClick : InterviewQuizEvent
}