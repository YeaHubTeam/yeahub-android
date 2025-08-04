package ru.yeahub.public_questions.impl.presentation

data class RequestPublicQuestionsData(
    val page: Int,
    val limit: Int,
    val skills: List<String>? = null,
    val skillFilter: String? = null
)
