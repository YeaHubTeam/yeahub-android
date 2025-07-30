package ru.yeahub.example_questions.impl.domain

import ru.yeahub.example_questions.impl.presentation.RequestQuestionsData
import ru.yeahub.network_api.models.GetPublicQuestionsResponse

class QuestionsUseCaseImpl(private val repository: QuestionsRepository) : QuestionsUseCase {

    override suspend fun invoke(request: RequestQuestionsData): GetPublicQuestionsResponse {
        return repository.getQuestions(request)
    }
}