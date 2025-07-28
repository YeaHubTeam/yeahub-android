package ru.yeahub.public_questions.impl.domain.entity

data class QuestionsModel(
    val page: Long?,
    val limit: Long?,
    val data: List<QuestionModel>,
    val total: Long
)
