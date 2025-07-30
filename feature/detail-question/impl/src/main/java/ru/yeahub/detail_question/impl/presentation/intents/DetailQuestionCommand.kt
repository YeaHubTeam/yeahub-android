package ru.yeahub.detail_question.impl.presentation.intents

internal sealed class DetailQuestionCommand {
    data object NavigateBack : DetailQuestionCommand()
    data object NavigatePrevPage : DetailQuestionCommand()
    data object NavigateNextPage : DetailQuestionCommand()
    data class OpenUrl(val url: String) : DetailQuestionCommand()
}