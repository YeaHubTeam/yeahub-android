package com.example.impl.domain.usecase

import com.example.impl.domain.models.PublicQuestionEntity
import com.example.impl.domain.repository.DetailQuestionRepository

class GetQuestionByIdUseCaseImpl(private val detailQuestionRepository: DetailQuestionRepository) :
    GetQuestionByIdUseCase {
    override suspend fun invoke(id: Long): PublicQuestionEntity {
        return detailQuestionRepository.getQuestionById(id)
    }

}