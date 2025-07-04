package com.example.impl.domain

import com.example.impl.presentation.RequestQuestionsData
import ru.yeahub.network_api.models.GetPublicQuestionsResponse

interface QuestionsRepository {
    suspend fun getQuestions(request: RequestQuestionsData): GetPublicQuestionsResponse
}