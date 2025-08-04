package ru.yeahub.detail_question.impl.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.yeahub.detail_question.impl.domain.models.PublicQuestionEntity
import ru.yeahub.detail_question.impl.domain.repository.DetailQuestionRepository

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