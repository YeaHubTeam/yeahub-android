package com.example.impl.presentation.intents

internal sealed class DetailQuestionCommand {
    data object Todo : DetailQuestionCommand()
}