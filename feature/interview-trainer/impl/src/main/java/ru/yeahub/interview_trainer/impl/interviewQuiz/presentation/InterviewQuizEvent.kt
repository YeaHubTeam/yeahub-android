package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

sealed interface InterviewQuizEvent {

    data object ToDo : InterviewQuizEvent
}