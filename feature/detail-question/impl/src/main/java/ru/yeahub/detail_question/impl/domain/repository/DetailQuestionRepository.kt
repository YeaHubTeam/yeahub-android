package ru.yeahub.detail_question.impl.domain.repository

import ru.yeahub.detail_question.impl.domain.models.PublicQuestionEntity

interface DetailQuestionRepository {
   suspend fun getQuestionById(id: Long): PublicQuestionEntity
}