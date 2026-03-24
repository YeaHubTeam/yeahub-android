package ru.yeahub.interview_trainer.impl.interviewQuizResult

sealed interface InterviewQuizResultEvent {

    data object Todo : InterviewQuizResultEvent // TODO: сделать ивенты
}