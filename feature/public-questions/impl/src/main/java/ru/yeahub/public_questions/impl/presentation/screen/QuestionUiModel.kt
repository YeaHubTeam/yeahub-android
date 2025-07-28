package ru.yeahub.public_questions.impl.presentation.screen

data class QuestionUiModel(
    val id: Long,
    val title: String,
    val rate: String,
    val complexity: String,
    val imageSc: String,
    val description: String
)
