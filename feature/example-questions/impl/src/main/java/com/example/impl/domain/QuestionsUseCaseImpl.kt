package com.example.impl.domain

import com.example.impl.presentation.RequestQuestionsData
import ru.yeahub.network_api.models.GetPublicQuestionsResponse

class QuestionsUseCaseImpl(private val repository: QuestionsRepository) : QuestionsUseCase {

    override suspend fun invoke(request: RequestQuestionsData): GetPublicQuestionsResponse {
        return repository.getQuestions(request)
    }
}