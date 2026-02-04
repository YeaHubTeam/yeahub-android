package ru.yeahub.interview_trainer.impl.interviewQuiz.presentation

sealed interface InterviewQuizCommand {

    data object ToDo : InterviewQuizCommand
}