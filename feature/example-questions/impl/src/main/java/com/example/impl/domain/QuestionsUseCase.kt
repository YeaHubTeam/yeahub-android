package com.example.impl.domain

import com.example.impl.presentation.RequestQuestionsData
import ru.yeahub.network_api.models.GetPublicQuestionsResponse

interface QuestionsUseCase {
    suspend fun invoke(request: RequestQuestionsData): GetPublicQuestionsResponse
}