package ru.yeahub.detail_question.impl.presentation.intents

internal sealed class DetailQuestionCommand {
    data object NavigateBack : DetailQuestionCommand()
    data class OpenUrl(val url: String) : DetailQuestionCommand()
    data class NavigateToQuestion(val newIndex: Int) : DetailQuestionCommand()
}