package com.example.impl.domain.usecase

import com.example.impl.domain.models.PublicQuestionEntity
import com.example.impl.domain.repository.DetailQuestionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetQuestionByIdUseCaseImpl(
    private val detailQuestionRepository: DetailQuestionRepository,
    private val dispatcher: CoroutineDispatcher
) :
    GetQuestionByIdUseCase {
    override suspend fun invoke(id: Long): PublicQuestionEntity {
        return withContext(dispatcher) {
            detailQuestionRepository.getQuestionById(id)
        }
    }
}