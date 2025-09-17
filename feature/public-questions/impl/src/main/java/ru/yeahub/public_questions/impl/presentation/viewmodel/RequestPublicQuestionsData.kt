package ru.yeahub.public_questions.impl.presentation.viewmodel

data class RequestPublicQuestionsData(
    val page: Int,
    val limit: Int,
    val skills: List<String>?,
    val skillFilter: String?,
    val idCollection: Int?
)