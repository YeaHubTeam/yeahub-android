package ru.yeahub.detail_question.impl.domain.usecase

import ru.yeahub.detail_question.impl.domain.models.PublicQuestionEntity

interface GetQuestionByIdUseCase {
    suspend operator fun invoke(id: Long): PublicQuestionEntity
}