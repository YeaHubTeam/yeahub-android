package ru.yeahub.public_questions.impl.domain.repository

import ru.yeahub.public_questions.impl.domain.entity.PublicQuestionsModel

interface PublicQuestionsRepositoryApi {

    suspend fun getPublicQuestionsQuestion(
        page: Int,
        limit: Int,
        skillFilter: String?,
        idCollection: Int?,
        idSpecialization: Int?
    ): PublicQuestionsModel
}