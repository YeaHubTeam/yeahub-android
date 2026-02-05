package ru.yeahub.detail_question.impl.presentation.intents

internal sealed class DetailQuestionEvent {
    data class LoadQuestion(val id: Long) : DetailQuestionEvent()
    data object OnBackClick : DetailQuestionEvent()
    data class OnTelegramClick(val url: String) : DetailQuestionEvent()
    data class OnYoutubeClick(val url: String) : DetailQuestionEvent()
    data class OnPreviousQuestionClick(val currentQuestionId: Long) : DetailQuestionEvent()
    data class OnNextQuestionClick(val currentQuestionId: Long) : DetailQuestionEvent()
}