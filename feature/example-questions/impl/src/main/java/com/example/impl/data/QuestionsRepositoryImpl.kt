package com.example.impl.data

import com.example.impl.domain.QuestionsRepository
import com.example.impl.presentation.RequestQuestionsData
import ru.yeahub.network_api.ApiService
import ru.yeahub.network_api.models.GetPublicQuestionsResponse

class QuestionsRepositoryImpl(private val apiService: ApiService) : QuestionsRepository {

    override suspend fun getQuestions(request: RequestQuestionsData): GetPublicQuestionsResponse {
        return apiService.getQuestions(request.page, request.limit)
    }
}