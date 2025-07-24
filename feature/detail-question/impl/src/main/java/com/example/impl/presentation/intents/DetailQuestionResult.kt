package com.example.impl.presentation.intents

sealed class DetailQuestionResult {
    data object BackClick : DetailQuestionResult()
    data object NextClick : DetailQuestionResult()
    data object PrevClick : DetailQuestionResult()
    data class  UrlClick(val url: String) : DetailQuestionResult()
}