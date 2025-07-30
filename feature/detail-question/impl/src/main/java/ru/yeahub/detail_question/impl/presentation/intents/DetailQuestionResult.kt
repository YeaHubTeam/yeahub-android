package ru.yeahub.detail_question.impl.presentation.intents

sealed class DetailQuestionResult {
    data object BackClick : DetailQuestionResult()
    data class NextClick(val nextId: Long) : DetailQuestionResult()
    data class PrevClick(val prevId: Long) : DetailQuestionResult()
    data class UrlClick(val url: String) : DetailQuestionResult()
}