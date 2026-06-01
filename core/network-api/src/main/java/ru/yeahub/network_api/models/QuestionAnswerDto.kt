package ru.yeahub.network_api.models

data class QuestionAnswerDto(
    val questionId: Long,
    val questionTitle: String,
    val answer: String
)
