package ru.yeahub.public_questions.impl.domain.repository

import ru.yeahub.public_questions.impl.domain.entity.QuestionsModel

interface QuestionsRepositoryApi {

    suspend fun getQuestion(
        page: Int,
        limit: Int,
        skills: List<String>?,
        skillFilter: String?
    ): QuestionsModel
}