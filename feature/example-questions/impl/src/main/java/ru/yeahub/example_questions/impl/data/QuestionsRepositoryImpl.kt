package ru.yeahub.example_questions.impl.data

import ru.yeahub.example_questions.impl.domain.QuestionsRepository
import ru.yeahub.example_questions.impl.presentation.RequestQuestionsData
import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.models.GetPublicQuestionsResponse

class QuestionsRepositoryImpl(private val apiService: ApiService) : QuestionsRepository {

    override suspend fun getQuestions(request: RequestQuestionsData): GetPublicQuestionsResponse {
        return apiService.getQuestions(request.page, request.limit)
    }
}