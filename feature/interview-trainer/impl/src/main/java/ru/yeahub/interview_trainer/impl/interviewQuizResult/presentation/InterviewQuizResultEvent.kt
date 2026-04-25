package ru.yeahub.interview_trainer.impl.interviewQuizResult.presentation

sealed interface InterviewQuizResultEvent {

    data object Todo : InterviewQuizResultEvent // TODO: сделать ивенты
}