package ru.yeahub.example_questions.impl.presentation.intents

sealed class QuestionMainScreenCommand {
    object NavigateToBaseQuestions : QuestionMainScreenCommand()
    object NavigateToCollections : QuestionMainScreenCommand()
}