package ru.yeahub.public_questions.impl.domain.entity

data class PublicQuestionsModel(
    val page: Long?,
    val limit: Long?,
    val data: List<PublicQuestionModel>,
    val total: Long
)
