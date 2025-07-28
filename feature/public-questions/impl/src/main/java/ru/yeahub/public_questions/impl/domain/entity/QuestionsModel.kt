package ru.yeahub.public_questions.impl.domain.entity

data class QuestionsModel(
    val id: Long,
    val title: String,
    val rate: String?,
    val complexity: String?,
    val imageSc: String?,
    val description: String,
)