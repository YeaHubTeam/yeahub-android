package com.example.impl.presentation.intents

internal sealed class DetailQuestionEvent  {
    data object Todo : DetailQuestionEvent()
}