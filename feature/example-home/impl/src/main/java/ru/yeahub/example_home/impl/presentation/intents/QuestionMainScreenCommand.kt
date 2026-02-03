package ru.yeahub.example_home.impl.presentation.intents

sealed class QuestionMainScreenCommand {
    object NavigateToBaseQuestions : QuestionMainScreenCommand()
    object NavigateToCollections : QuestionMainScreenCommand()
    object NavigateToInterviewTrainer : QuestionMainScreenCommand()
}